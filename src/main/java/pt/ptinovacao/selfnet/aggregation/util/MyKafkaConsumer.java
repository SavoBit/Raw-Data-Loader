package pt.ptinovacao.selfnet.aggregation.util;

import org.apache.kafka.clients.consumer.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author rui-d-pedro
 */
public class MyKafkaConsumer implements Serializable {
	/*
		Defaults
	 */
	private static final String DEF_BOOTSTRAP_SERVER = "localhost:9092";
	private static final int DEF_POLL_TIMEOUT = 1000;
	private static final boolean DEF_AUTOCOMMIT = true;
	private static final int DEF_RECORDS = 10000000;
	private static final String DEF_GROUP_ID = "group_1";
	private static final int DEF_COMMIT_INTERVAL = 100;
	//by default kafka uses RangeAssignor, however RoundRobinAssignor seems to be always a better choice. (should solve partition re-balance problems)
	private static final String DEF_STRATEGY = "org.apache.kafka.clients.consumer.RoundRobinAssignor";

	/*
		Properties
	 */
	private final Properties config = new Properties();
	//consumer poll timeout
	private final int POLL_TIMEOUT;
	//consumer AUTOCOMMIT flag
	private final boolean AUTOCOMMIT;
	//maximum RECORDS that a consumer can poll in one go
	private final int RECORDS;
	//consumer group id
	private final String GROUP_ID;
	//consumer commit interval
	private final int COMMIT_INTERVAL;
	//bootstrap kafka server
	private final String BOOTSTRAP_SERVERS;
	//partition assignor strategy
	private final String ASSIGNOR_STRATEGY;

	//consumer
	private final Consumer<String, String> consumer;

	/**
	 *
	 */
	public MyKafkaConsumer() {
		this(DEF_BOOTSTRAP_SERVER, DEF_AUTOCOMMIT, DEF_RECORDS, DEF_GROUP_ID, DEF_COMMIT_INTERVAL, DEF_POLL_TIMEOUT);
	}

	/**
	 * @param bootstrapServers
	 */
	public MyKafkaConsumer(String bootstrapServers) {
		this(bootstrapServers, DEF_AUTOCOMMIT, DEF_RECORDS, DEF_GROUP_ID, DEF_COMMIT_INTERVAL, DEF_POLL_TIMEOUT);
	}

	/**
	 * @param bootstrapServers
	 * @param autocommit
	 */
	public MyKafkaConsumer(String bootstrapServers, boolean autocommit) {
		this(bootstrapServers, autocommit, DEF_RECORDS, DEF_GROUP_ID, DEF_COMMIT_INTERVAL, DEF_POLL_TIMEOUT);
	}

	/**
	 * @param bootstrapServers
	 * @param autocommit
	 * @param records
	 */
	public MyKafkaConsumer(String bootstrapServers, boolean autocommit, int records) {
		this(bootstrapServers, autocommit, records, DEF_GROUP_ID, DEF_COMMIT_INTERVAL, DEF_POLL_TIMEOUT);
	}

	/**
	 * @param bootstrapServers
	 * @param autocommit
	 * @param records
	 * @param groupID
	 */
	public MyKafkaConsumer(String bootstrapServers, boolean autocommit, int records, String groupID) {
		this(bootstrapServers, autocommit, records, groupID, DEF_COMMIT_INTERVAL, DEF_POLL_TIMEOUT);
	}

	/**
	 * @param bootstrapServers
	 * @param autocommit
	 * @param records
	 * @param groupID
	 * @param commitInterval
	 */
	public MyKafkaConsumer(String bootstrapServers, boolean autocommit, int records, String groupID, int commitInterval) {
		this(bootstrapServers, autocommit, records, groupID, commitInterval, DEF_POLL_TIMEOUT);
	}

	/**
	 * @param bootstrapServers
	 * @param autocommit
	 * @param records
	 * @param groupID
	 * @param commitInterval
	 * @param poolTimeout
	 */
	public MyKafkaConsumer(String bootstrapServers, boolean autocommit, int records, String groupID, int commitInterval, int poolTimeout) {
		this(bootstrapServers, autocommit, records, groupID, commitInterval, poolTimeout, DEF_STRATEGY);
	}

	/**
	 * @param bootstrapServers
	 * @param autocommit
	 * @param records
	 * @param groupID
	 * @param commitInterval
	 * @param poolTimeout
	 * @param strategy
	 */
	public MyKafkaConsumer(String bootstrapServers, boolean autocommit, int records, String groupID, int commitInterval, int poolTimeout, String strategy) {
		this.POLL_TIMEOUT = poolTimeout;
		this.AUTOCOMMIT = autocommit;
		this.RECORDS = records;
		this.GROUP_ID = groupID;
		this.COMMIT_INTERVAL = commitInterval;
		this.BOOTSTRAP_SERVERS = bootstrapServers;
		this.ASSIGNOR_STRATEGY = strategy;

		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		config.put(ConsumerConfig.GROUP_ID_CONFIG, groupID);
		config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autocommit);
		config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, records);
		config.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, commitInterval);
		config.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, strategy);
//		config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,Integer.valueOf("30000"));

        /* other configuration that might be useful
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        config.put(ConsumerConfig.CLIENT_ID_CONFIG, id);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, id);
        config.put(ConsumerConfig.METADATA_MAX_AGE_CONFIG, Long.toString(maxAge));
        config.put(ConsumerConfig.SEND_BUFFER_CONFIG, Integer.toString(size));
        config.put(ConsumerConfig.RECEIVE_BUFFER_CONFIG, Integer.toString(size));
        config.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, Integer.toString(interval));
        config.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, Integer.toString(interval));
        config.put(ConsumerConfig.METRICS_NUM_SAMPLES_CONFIG, Integer.toString(samples));
        config.put(ConsumerConfig.METRICS_SAMPLE_WINDOW_MS_CONFIG, Integer.toString(window));
        config.put(ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, Integer.toString(time));
        config.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, Integer.toString(timeout));
        config.put(ConsumerConfig.METADATA_MAX_AGE_CONFIG, Integer.toString(maxAge));

        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,String.valueOf(enable));
        config.put(ConsumerConfig.CHECK_CRCS_CONFIG,Boolean.valueOf(enable));
        config.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,Integer.valueOf(interval));
        config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,Integer.valueOf(timeout));
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,cls);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,cls);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,resetMode.toString());
        config.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,strategy);
        config.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG,clss);
        config.put(ConsumerConfig.METRIC_REPORTER_CLASSES_CONFIG,clss);
        config.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG,Integer.valueOf(interval));
        config.put(ConsumerConfig.EXCLUDE_INTERNAL_TOPICS_CONFIG,Boolean.valueOf(exclude));
        config.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG,Integer.valueOf(maxWait));
        config.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG,Integer.valueOf(bytes));
        config.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG,Integer.valueOf(bytes));
        config.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,Integer.valueOf(bytes));
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,Integer.valueOf(RECORDS));
        */

		consumer = new KafkaConsumer<String, String>(config);
	}

	public void consumePrint(String[] topics) {
		consumer.subscribe(Arrays.asList(topics));

		List<ConsumerRecord<String, String>> buffer = new ArrayList<ConsumerRecord<String, String>>();

		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(POLL_TIMEOUT);

			if (AUTOCOMMIT) {
				for (ConsumerRecord<String, String> record : records) {
					System.out.printf("Key:[%s] Message[%s%n], from Topic[%s] with Offset[%d]", record.key(), record.value(), record.topic(), record.offset());
				}
			} else {
				// Needs to handle storage of the individual offsets plus commit them sync/async
			}
		}
	}

	public synchronized ConsumerRecords<String, String> consume(String[] topics) {
		consumer.subscribe(Arrays.asList(topics));
		// Needs to handle storage of the individual offsets plus commit them sync/async
		return consumer.poll(POLL_TIMEOUT);
	}

	public void close() {
		consumer.close();
	}
}