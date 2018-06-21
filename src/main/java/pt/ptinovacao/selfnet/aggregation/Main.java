package pt.ptinovacao.selfnet.aggregation;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import pt.ptinovacao.selfnet.aggregation.configuration.*;
import pt.ptinovacao.selfnet.aggregation.constants.DefaultConfig;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * TODO - extract interfaces for better modularity
 *
 * @author rui-d-pedro
 */
public class Main {
	//Logger
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String... args) {
		//cli options
		Options options = new Options();
		options.addOption("h", false, "print this help message");
		options.addOption("c", true, "yaml configuration file");

		HelpFormatter formatter = new HelpFormatter();

		CommandLineParser cliParser = new DefaultParser();
		CommandLine cmd;

		//default config file path
		String filepath = DefaultConfig.configFilepath;

		try {
			cmd = cliParser.parse(options, args);
			for (Option o : cmd.getOptions()) {
				switch (o.getOpt()) {
					//help
					case "h":
						formatter.printHelp(" ", options);
						System.exit(0);
						break;

					//config filepath
					case "c":
						//retrive the filepath from args
						filepath = o.getValue().toString();

						//check if the path exists
						if (!Files.exists(Paths.get(filepath), LinkOption.NOFOLLOW_LINKS)) {
							LOGGER.error("Specified filepath does not exist.");
							System.exit(0);
						}
						//check if the path is not a directory
						if (Files.isDirectory(Paths.get(filepath))) {
							LOGGER.error("Specified filepath should be a file and not a directory.");
							System.exit(0);
							break;
						}
						//check the file is readable
						if (!Files.isReadable(Paths.get(filepath))) {
							LOGGER.error("Specified file is not readable.");
							System.exit(0);
						}
						break;
					default:
						formatter.printHelp(" ", options);
						System.exit(0);
						break;
				}
			}
		} catch (ParseException e) {
			System.out.println("Invalid arguments. Check -h for help");
			System.exit(0);
		}

		//instantiate a rdl
		RDL rdl = new RDL(loadConfig(filepath));

		//start rdl
		new Thread(rdl).start();
	}

	/**
	 * Loads configuration from filepath. If it cannot be loaded or the loaded configuration is not valid
	 * it will load the default configuration
	 *
	 * @param filepath
	 * @return Configuration
	 */
	private static Configuration loadConfig(String filepath) {
		boolean failed = false;

		Configuration config = new Configuration();

		Yaml yaml = new Yaml();

		try {
			InputStream in = Files.newInputStream(Paths.get(filepath));
			config = yaml.loadAs(in, Configuration.class);
			in.close();
			LOGGER.info("Configuration file successfully loaded");
		} catch (IOException e) {
			//TODO - change to logger
			e.printStackTrace();
			//failed to load config from the supplied yaml file
			failed = true;

		} finally {
			if (!config.isValid() || failed) {
				//failed to load config from the supplied yaml file
				//OR
				//the loaded configuration is not valid
				LOGGER.warn("failed to load config or the loaded configuration is not valid, loading the default configuration");

				//so we'll load the default configuration
				config = loadDefaultConfig();
			}
		}
		return config;
	}

	/**
	 * Load default configuration
	 *
	 * @return Default configuration
	 */
	private static Configuration loadDefaultConfig() {
		Configuration config = new Configuration();
		config.setVersion("default");

		KafkaConfiguration kafkaConfig = new KafkaConfiguration();
		kafkaConfig.setBootstrapserver(DefaultConfig.KAFKA_BOOTSTRAPSERVER);
		kafkaConfig.setTopics(Arrays.asList(DefaultConfig.KAFKA_TOPICS));
		kafkaConfig.setAutocommit(DefaultConfig.KAFKA_AUTOCOMMIT);
		kafkaConfig.setRecords(DefaultConfig.KAFKA_RECORDS);
		kafkaConfig.setGroupID(DefaultConfig.KAFKA_GROUPID);
		kafkaConfig.setPoolTimeout(DefaultConfig.KAFKA_POOLTIMEOUT);
		kafkaConfig.setCommitInterval(DefaultConfig.KAFKA_COMMITINTERVAL);

		config.setKafka(kafkaConfig);

		CassandraConfiguration cassandraConfig = new CassandraConfiguration();
		cassandraConfig.setTableCounters(DefaultConfig.CASSANDRA_TABLE_COUNTERS);
		cassandraConfig.setTableEvents(DefaultConfig.CASSANDRA_TABLE_EVENTS);
		cassandraConfig.setKeyspace(DefaultConfig.CASSANDRA_KEYSPACE);
		cassandraConfig.setUser(DefaultConfig.CASSANDRA_USER);
		cassandraConfig.setPassword(DefaultConfig.CASSANDRA_PASSWORD);
		cassandraConfig.setHost(DefaultConfig.CASSANDRA_HOST);
		cassandraConfig.setPort(DefaultConfig.CASSANDRA_PORT);

		config.setCassandra(cassandraConfig);

		RDLConfiguration rdlConfig = new RDLConfiguration();
		rdlConfig.setJointimeout(DefaultConfig.RDL_JOINTIMEOUT);
		rdlConfig.setAsync(DefaultConfig.RDL_ASYNC);

		rdlConfig.setDebug(DefaultConfig.RDL_DEBUG);

		RDLCollectorConfiguration rdlCollectorConfig = new RDLCollectorConfiguration();
		rdlCollectorConfig.setPollingtime(DefaultConfig.RDL_COLLECTORPOLLINGTIME);
		rdlCollectorConfig.setThreads(DefaultConfig.RDL_COLLECTORTHREADS);

		rdlConfig.setCollector(rdlCollectorConfig);

		RDLPersisterConfiguration rdlPersisterConfig = new RDLPersisterConfiguration();
		rdlPersisterConfig.setPollingtime(DefaultConfig.RDL_PERSISTERPOLLINGTIME);
		rdlPersisterConfig.setThreads(DefaultConfig.RDL_PERSISTERTHREADS);

		rdlConfig.setPersister(rdlPersisterConfig);

		config.setRdl(rdlConfig);

		return config;
	}
}
