package pt.ptinovacao.selfnet.aggregation;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.AuthenticationException;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ptinovacao.selfnet.aggregation.configuration.Configuration;
import pt.ptinovacao.selfnet.aggregation.constants.ModelClasses;
import pt.ptinovacao.selfnet.aggregation.models.Counter;
import pt.ptinovacao.selfnet.aggregation.models.Event;
import pt.ptinovacao.selfnet.aggregation.util.MyKafkaConsumer;
import pt.ptinovacao.selfnet.aggregation.util.Parser;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Thread.sleep;

/**
 * TODO - add logging
 *
 * @author rui-d-pedro
 */
public class RDL implements Runnable {
	//Logger
	private static final Logger LOGGER = LoggerFactory.getLogger(RDL.class);

	//RDL configuration
	private Configuration configuration;

	/*
		Kafka consumer settings
	*/
	private String[] topics;
	private String bootstrapServers;
	private boolean autocommit;
	private int records;
	private String groupID;
	private int commitInterval;
	private int poolTimeout;
	/*
		Cassandra driver settings
	 */
	private String cassandraHost;
	private int cassandraPort;
	private String cassandraUser;
	private String cassandraPassword;
	private String cassandraKeyspace;
	private String cassandraTableCounters;
	private String cassandraTableEvents;

	/*
		RDL settings
	 */
	private boolean running;
	private long joinTimeout;
	private boolean async;
	private boolean debug;
	private long collectorPollingTime;
	private long persisterPollingTime;
	private int collectorThreads;
	private int persisterThreads;


	/*
		RDL Objects and Data Structures
	*/
	//Concurrent Set List of kafka records (their content)
	private ConcurrentSkipListSet<String> recordsPile;

	//tasks
	private Runnable collectorTask;
	private Runnable persisterTask;
	private Runnable debugTask;

	private static AtomicInteger collectorCount = new AtomicInteger(0);

	//thread list
	private ArrayList<Thread> threadsList;
	//kafka consumer
	private MyKafkaConsumer consumer;
	//cassandra cluster
	private Cluster cluster;

	/*
		Debugging variables
	*/
	//Debugger report timer
	AtomicLong superstart;
	//DB inserts counter
	AtomicInteger inserts;
	//kafka consumer consumed messages
	AtomicInteger consumed;

	AtomicInteger lost;

	public RDL(Configuration configuration) {
		this.configuration = configuration;
		this.threadsList = new ArrayList<>();

		topics = this.configuration.getKafka().getTopics().parallelStream().toArray(String[]::new);
		bootstrapServers = this.configuration.getKafka().getBootstrapserver();
		autocommit = this.configuration.getKafka().getAutocommit();
		records = this.configuration.getKafka().getRecords();
		groupID = this.configuration.getKafka().getGroupID();
		commitInterval = this.configuration.getKafka().getCommitInterval();
		poolTimeout = this.configuration.getKafka().getPoolTimeout();

		cassandraHost = this.configuration.getCassandra().getHost();
		cassandraPort = Integer.parseInt(this.configuration.getCassandra().getPort());
		cassandraKeyspace = this.configuration.getCassandra().getKeyspace();
		cassandraPassword = this.configuration.getCassandra().getPassword();
		cassandraUser = this.configuration.getCassandra().getUser();
		cassandraTableCounters = this.configuration.getCassandra().getTableCounters();
		cassandraTableEvents = this.configuration.getCassandra().getTableEvents();

		//a small trick to change the final static table name value of the Counter Table annotation
		Counter.setTable(cassandraTableCounters);
		Event.setTable(cassandraTableEvents);

		this.running = true;

		this.async = this.configuration.getRdl().getAsync();
		this.debug = this.configuration.getRdl().getDebug();
		this.collectorPollingTime = this.configuration.getRdl().getCollector().getPollingtime();
		this.collectorThreads = this.configuration.getRdl().getCollector().getThreads();
		this.persisterPollingTime = this.configuration.getRdl().getPersister().getPollingtime();
		this.persisterThreads = this.configuration.getRdl().getPersister().getThreads();
		this.joinTimeout = this.configuration.getRdl().getJointimeout();

		this.recordsPile = new ConcurrentSkipListSet<>();

		this.superstart = new AtomicLong(System.currentTimeMillis());
		this.inserts = new AtomicInteger(0);
		this.consumed = new AtomicInteger(0);
		this.lost = new AtomicInteger(0);
	}

	/**
	 * TODO - extract the tasks into classes
	 * Setup Runnable tasks
	 */
	private final void setupTasks() {
		//setup collector task
		collectorTask = () ->
		{
			//instantiate a Kafka consumer
			consumer = new MyKafkaConsumer(bootstrapServers, autocommit, records, groupID + collectorCount.incrementAndGet(), commitInterval, poolTimeout);

			while (running) {
				//consume from a list of topics
				ConsumerRecords<String, String> records = consumer.consume(topics);

				//accumulate the count of consumed messages if debug enabled
				if (debug) {
					consumed.accumulateAndGet(records.count(), (left, right) -> left + right);
				}

				//consume from kafka, note that if there aren't any records to be polled,
				// the consume method returns an empty map
				records.forEach(record ->
				{
					//add the consumed records to a pile of records so they can be processed later
					recordsPile.add(record.value());
				});

				//yield the CPU a bit
				try {
					sleep(collectorPollingTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};


		//setup persister task
		persisterTask = () ->
		{
			//Cassandra cluster
			cluster = Cluster.builder()
							 .addContactPoint(cassandraHost)
							 .withPort(cassandraPort)
							 .withCredentials(cassandraUser, cassandraPassword)
							 .build();

			//Cassandra session
			Session session = cluster.connect(cassandraKeyspace);
			//Cassandra manager
			MappingManager manager = new MappingManager(session);
			//Cassandra counterMapper (is the ORM - Object-relational mapping tool)
			Mapper<Counter> counterMapper = manager.mapper(Counter.class);
			Mapper<Event> eventMapper = manager.mapper(Event.class);


			while (running) {
				if (!recordsPile.isEmpty()) {
					recordsPile.parallelStream().forEach(record ->
							{
								recordsPile.remove(record);
								//TODO - possibly optimize this with a concurrent data set
								//parse the json string record into a list of Counter object
								try {
									Parser.parse(record).forEach(model ->
									{
										//TODO - optimize + add proper error checking and recover
										switch (ModelClasses.valueOfEnum(model.getClass())) {
											case COUNTER_CLASS: {
												//save it into Cassandra
												if (async) {
													//save async
													counterMapper.saveAsync(((Counter) model));
												} else {
													//save sync
													counterMapper.save(((Counter) model));
												}
												//increment the inserts counter for debugging purposes
												if (debug) {
													inserts.incrementAndGet();
												}
												break;
											}
											case EVENT_CLASS: {
												//save it into Cassandra
												if (async) {
													//save async
													eventMapper.saveAsync(((Event) model));
												} else {
													//save sync
													eventMapper.save(((Event) model));
												}
												//increment the inserts counter for debugging purposes
												if (debug) {
													inserts.incrementAndGet();
												}
												break;
											}
											case DEFAULT: {
												LOGGER.error("Couldn't find model class");
												if (debug) {
													lost.incrementAndGet();
												}
												break;
											}
										}
									});
								} catch (Exception e) {
									LOGGER.error(e.getMessage());
									if (debug) {
										lost.incrementAndGet();
									}
								}

								//assuming that everything went well and the json was mapped successfully,
								//therefore this record can be removed from the pile
								recordsPile.remove(record);
							}
					);
				} else {
					try {
						sleep(persisterPollingTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		//setup debug task
		debugTask = () ->
		{
			while (running) {
				long now = System.currentTimeMillis();
				//report in 1 second periods
				if (now - superstart.get() >= 1000) {
//					LOGGER.debug("Consumed {}\n", consumed.get());
//					LOGGER.debug("Doing {} inserts per second | records left {} in the pile\n", inserts.get(), recordsPile.size());

//					System.out.printf("Consumed %d\n", consumed.get());
//					System.out.printf("Doing %10d inserts per second | records left %10d in the pile\n", inserts.get(), recordsPile.size());

					String reportString = "|T=%13d|Consumed=%10d|Queued=%10d|Inserted=%10d|Lost=%10d|";
					LOGGER.debug(reportString, now, consumed.get(), recordsPile.size(), inserts.get(), lost.get());
					System.out.printf(reportString + "\n", now, consumed.get(), recordsPile.size(), inserts.get(), lost.get());

					//reset consumed
					consumed.set(0);
					//reset timer
					superstart.set(System.currentTimeMillis());
					//reset inserts counter
					inserts.set(0);
					//reset lost
					lost.set(0);

				} else {
					try {
						sleep(now - superstart.get());
					} catch (InterruptedException e) {
//						e.printStackTrace();
					}
				}
			}
		};
	}

	/**
	 * Setup all threads
	 */
	private final void setupThreads() {
		//Collector thread
		for (int i = 0; i < collectorThreads; i++) {
			threadsList.add(new Thread(collectorTask, "rdl-collector" + i));
		}

		//Persister thread
		for (int i = 0; i < persisterThreads; i++) {
			threadsList.add(new Thread(persisterTask, "rdl-persister" + i));
		}

		//If debug enabled active the debugger
		if (debug) {
			threadsList.add(new Thread(debugTask, "rdl-debugger"));
		}
	}

	/**
	 * Setup RDL
	 */
	private final void setup() {
		setupTasks();
		setupThreads();
	}

	@Override
	public void run() {
		//setup
		setup();
		//start all threads
		threadsList.parallelStream().forEach(Thread::start);

		//wait all threads to die
		join();
	}

	/**
	 * finalize and stop this RDL if it is running as a Thread
	 *
	 * @throws Throwable
	 */
	@Override
	protected void finalize() throws Throwable {
		stop();
		super.finalize();
	}

	/**
	 * Stop and then close() all threads
	 */
	private void stop() {
		//stop threads
		running = false;
		close();
	}

	/**
	 * Close Cassandra cluster and Kafka consumer
	 */
	private void close() {
		join();
		cluster.close();
		consumer.close();
	}

	/**
	 * Thread.join for every thread created by RDL
	 */
	private void join() {
		while (!threadsList.isEmpty()) {
			threadsList.forEach(thread ->
			{
				if (!thread.isAlive())
					threadsList.remove(thread);
				else
					try {
						thread.join(joinTimeout);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			});
		}
	}

	/**
	 * Returns a boolean indicating if this RDL is running or not
	 *
	 * @return
	 */
	public boolean isRunning() {
		return running;
	}
}
