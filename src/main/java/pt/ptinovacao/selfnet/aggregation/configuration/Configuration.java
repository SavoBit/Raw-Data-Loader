package pt.ptinovacao.selfnet.aggregation.configuration;

/**
 * @author rui-d-pedro
 */
public class Configuration {
	private String version;
	private KafkaConfiguration kafka;
	private CassandraConfiguration cassandra;
	private RDLConfiguration rdl;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public KafkaConfiguration getKafka() {
		return kafka;
	}

	public void setKafka(KafkaConfiguration kafka) {
		this.kafka = kafka;
	}

	public CassandraConfiguration getCassandra() {
		return cassandra;
	}

	public void setCassandra(CassandraConfiguration cassandra) {
		this.cassandra = cassandra;
	}

	public RDLConfiguration getRdl() {
		return rdl;
	}

	public void setRdl(RDLConfiguration rdl) {
		this.rdl = rdl;
	}

	@Override
	public String toString() {
		return "Configuration{" +
				"version='" + version + '\'' +
				", kafka=" + kafka +
				", cassandra=" + cassandra +
				", rdl=" + rdl +
				'}';
	}

	public boolean isValid() {
		boolean versionValid = false;
		if (version != null && !version.isEmpty())
			versionValid = true;

		boolean kafkaValid = false;
		if (kafka != null)
			kafkaValid = kafka.isValid();

		boolean cassandraValid = false;
		if (cassandra != null)
			cassandraValid = cassandra.isValid();

		boolean rdlValid = false;
		if (rdl != null)
			rdlValid = rdl.isValid();

		return versionValid || kafkaValid || cassandraValid || rdlValid;
	}
}
