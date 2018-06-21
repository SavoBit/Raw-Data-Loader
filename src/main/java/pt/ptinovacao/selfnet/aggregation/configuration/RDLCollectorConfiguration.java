package pt.ptinovacao.selfnet.aggregation.configuration;

/**
 * @author rui-d-pedro
 */
public class RDLCollectorConfiguration {
	private Long pollingtime;
	private Integer threads;

	public Long getPollingtime() {
		return pollingtime;
	}

	public void setPollingtime(Long pollingtime) {
		this.pollingtime = pollingtime;
	}

	public Integer getThreads() {
		return threads;
	}

	public void setThreads(Integer threads) {
		this.threads = threads;
	}

	@Override
	public String toString() {
		return "RDLCollectorConfiguration{" +
				"pollingtime=" + pollingtime +
				", threads=" + threads +
				'}';
	}

	public boolean isValid() {
		boolean pollingtimeValid = false;
		if (pollingtime != null)
			pollingtimeValid = true;

		boolean threadsValid = false;
		if (threads != null)
			threadsValid = true;

		return pollingtimeValid || threadsValid;
	}
}
