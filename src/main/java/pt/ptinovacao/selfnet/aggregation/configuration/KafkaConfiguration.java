package pt.ptinovacao.selfnet.aggregation.configuration;

import java.util.List;

/**
 * @author rui-d-pedro
 */
public class KafkaConfiguration {
	private String bootstrapserver;
	private List<String> topics;
	private Boolean autocommit;
	private Integer records;
	private String groupID;
	private Integer commitInterval;
	private Integer poolTimeout;

	public String getBootstrapserver() {
		return bootstrapserver;
	}

	public void setBootstrapserver(String bootstrapserver) {
		this.bootstrapserver = bootstrapserver;
	}

	public List<String> getTopics() {
		return topics;
	}

	public void setTopics(List<String> topics) {
		this.topics = topics;
	}

	public Boolean getAutocommit() {
		return autocommit;
	}

	public void setAutocommit(Boolean autocommit) {
		this.autocommit = autocommit;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
	}

	public String getGroupID() {
		return groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public int getCommitInterval() {
		return commitInterval;
	}

	public void setCommitInterval(int commitInterval) {
		this.commitInterval = commitInterval;
	}

	public int getPoolTimeout() {
		return poolTimeout;
	}

	public void setPoolTimeout(int poolTimeout) {
		this.poolTimeout = poolTimeout;
	}

	@Override
	public String toString() {
		return "KafkaConfiguration{" +
				"bootstrapserver='" + bootstrapserver + '\'' +
				", topics=" + topics +
				", autocommit=" + autocommit +
				", records=" + records +
				", groupID='" + groupID + '\'' +
				", commitInterval=" + commitInterval +
				", poolTimeout=" + poolTimeout +
				'}';
	}

	public boolean isValid() {
		boolean bootstrapserverValid = false;
		if (bootstrapserver != null && !bootstrapserver.isEmpty())
			bootstrapserverValid = true;

		boolean topicsValid = false;
		if (topics != null && !topics.isEmpty()) {
			topicsValid = topics.parallelStream().allMatch(topic -> !topic.isEmpty());
		}

		boolean autocommitValid = false;
		if (autocommit != null)
			autocommitValid = true;

		boolean recordsValid = false;
		if(records != null)
			recordsValid = true;

		boolean groupIDValid = false;
		if(groupID != null && !groupID.isEmpty())
			groupIDValid = true;

		boolean commitIntervalValid = false;
		if(commitInterval != null)
			commitIntervalValid = true;

		boolean poolTimeoutValid = false;
		if(poolTimeout != null)
			poolTimeoutValid = true;

		return bootstrapserverValid || topicsValid || autocommitValid || recordsValid || groupIDValid || commitIntervalValid || poolTimeoutValid;
	}
}
