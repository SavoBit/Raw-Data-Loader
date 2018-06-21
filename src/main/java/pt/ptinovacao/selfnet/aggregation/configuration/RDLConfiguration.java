package pt.ptinovacao.selfnet.aggregation.configuration;

/**
 * @author rui-d-pedro
 */
public class RDLConfiguration
{
	private Long jointimeout;
	private Boolean async;
	private Boolean debug;
	private RDLCollectorConfiguration collector;
	private RDLPersisterConfiguration persister;

	public Long getJointimeout() {
		return jointimeout;
	}

	public void setJointimeout(Long jointimeout) {
		this.jointimeout = jointimeout;
	}

	public Boolean getAsync() {
		return async;
	}

	public void setAsync(Boolean async) {
		this.async = async;
	}

	public Boolean getDebug() {
		return debug;
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	public RDLCollectorConfiguration getCollector() {
		return collector;
	}

	public void setCollector(RDLCollectorConfiguration collector) {
		this.collector = collector;
	}

	public RDLPersisterConfiguration getPersister() {
		return persister;
	}

	public void setPersister(RDLPersisterConfiguration persister) {
		this.persister = persister;
	}

	@Override
	public String toString() {
		return "RDLConfiguration{" +
				"jointimeout=" + jointimeout +
				", async=" + async +
				", debug=" + debug +
				", collector=" + collector +
				", persister=" + persister +
				'}';
	}

	public boolean isValid()
	{
		boolean jointimeoutValid = false;
		if(jointimeout != null)
			jointimeoutValid = true;

		boolean asyncValid = false;
		if(async != null)
			asyncValid = true;

		boolean debugValid = false;
		if(debug != null)
			debugValid = true;

		boolean collectorValid = false;
		if(collector != null)
			collectorValid = collector.isValid();

		boolean persisterValid = false;
		if(persister != null)
			persisterValid = persister.isValid();

		return jointimeoutValid || asyncValid || debugValid || collectorValid || persisterValid;
	}
}
