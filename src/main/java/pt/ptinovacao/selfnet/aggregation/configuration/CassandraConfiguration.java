package pt.ptinovacao.selfnet.aggregation.configuration;

/**
 * @author rui-d-pedro
 */
public class CassandraConfiguration
{
	private String tableCounters;
	private String tableEvents;
	private String keyspace;
	private String user;
	private String password;
	private String host;
	private String port;

	public String getTableCounters() {
		return tableCounters;
	}

	public void setTableCounters(String tableCounters) {
		this.tableCounters = tableCounters;
	}

	public String getTableEvents() {
		return tableEvents;
	}

	public void setTableEvents(String tableEvents) {
		this.tableEvents = tableEvents;
	}

	public String getKeyspace() {
		return keyspace;
	}

	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "CassandraConfiguration{" +
				"tableCounters='" + tableCounters + '\'' +
				", tableEvents='" + tableEvents + '\'' +
				", keyspace='" + keyspace + '\'' +
				", user='" + user + '\'' +
				", password='" + password + '\'' +
				", host='" + host + '\'' +
				", port='" + port + '\'' +
				'}';
	}

	public boolean isValid()
	{
		boolean tableCountersValid = false;
		if(tableCounters != null && !tableCounters.isEmpty())
			tableCountersValid = true;

		boolean tableEventsValid = false;
		if(tableEvents != null && !tableEvents.isEmpty())
			tableEventsValid = true;

		boolean keyspaceValid = false;
		if(keyspace != null && !keyspace.isEmpty())
			keyspaceValid = true;

		boolean userValid = false;
		if(user != null && !user.isEmpty())
			userValid = true;

		boolean passwordValid = false;
		if(password != null && !password.isEmpty())
			passwordValid = true;

		boolean hostValid = false;
		if(host != null && !host.isEmpty())
			hostValid = true;

		boolean portValid = false;
		if(port != null && !port.isEmpty())
			portValid = true;

		return tableCountersValid || tableEventsValid || keyspaceValid || userValid || passwordValid || hostValid || portValid;
	}
}
