package pt.ptinovacao.selfnet.aggregation.constants;

/**
 * @author rui-d-pedro
 */
public class DefaultConfig {
	/*
		Cassandra driver settings
	 */
	public static final String CASSANDRA_TABLE_COUNTERS = "selfnet_counters";
	public static final String CASSANDRA_TABLE_EVENTS = "selfnet_events";
	public static final String CASSANDRA_KEYSPACE = "selfnet";
	public static final String CASSANDRA_USER = "selfnet";
	public static final String CASSANDRA_PASSWORD = "selfnet";
	public static final String CASSANDRA_HOST = "localhost";
	public static final String CASSANDRA_PORT = "9042";

	/*
		Kafka consumer settings
	 */
	public static final String[] KAFKA_TOPICS = {"test"};
	public static final String KAFKA_BOOTSTRAPSERVER = "localhost:9092";
	public static final boolean KAFKA_AUTOCOMMIT = true;
	public static final int KAFKA_RECORDS = 10000000;
	public static final String KAFKA_GROUPID = "group_1";
	public static final int KAFKA_COMMITINTERVAL = 100;
	public static final int KAFKA_POOLTIMEOUT = 1000;

	/*
		RDL settings
	 */
	public static final String configFilepath = "./src/rdl/src/main/resources/rdl-config.yaml";
	public static final long RDL_JOINTIMEOUT = 1000;
	public static final boolean RDL_ASYNC = true;
	public static final boolean RDL_DEBUG = true;
	public static final long RDL_COLLECTORPOLLINGTIME = 5;
	public static final long RDL_PERSISTERPOLLINGTIME = 100;
	public static final int RDL_PERSISTERTHREADS = 1;
	public static final int RDL_COLLECTORTHREADS = 1;
}