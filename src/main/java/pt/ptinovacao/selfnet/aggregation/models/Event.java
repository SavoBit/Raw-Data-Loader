package pt.ptinovacao.selfnet.aggregation.models;

import com.datastax.driver.mapping.annotations.*;
import com.fasterxml.uuid.Generators;
import pt.ptinovacao.selfnet.aggregation.constants.DefaultConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author rui-d-pedro
 */
@Table(name = DefaultConfig.CASSANDRA_TABLE_EVENTS)
public class Event extends BaseModel implements Comparable {
	//internal model uuid for object comparison
	@Transient
	private UUID uuid;

	@PartitionKey(0)
	@Column(name = "dataType")
	private String dataType;

	@PartitionKey(1)
	@Column(name = "resourceType")
	private String resourceType;

	@PartitionKey(2)
	@Column(name = "timepartition")
	private Date timepartition;

	@ClusteringColumn(0)
	@Column(name = "timestamp")
	private Date timestamp;

	@ClusteringColumn(1)
	@Column(name = "resourceId")
	private String resourceID;

	@ClusteringColumn(2)
	@Column(name = "reporterId")
	private String reporterID;

	@Column(name = "resourceDescription")
	private Map<String, String> resourceDescription;

	@Column(name = "dataDefinition")
	private Map<String, String> dataDefinition;

	@Column(name = "reporterDescription")
	private Map<String, String> reporterDescription;

	/**
	 * Constructor
	 *
	 * @param dataType
	 * @param resourceType
	 * @param timestamp
	 * @param resourceID
	 * @param reporterID
	 * @param resourceDescription
	 * @param dataDefinition
	 * @param reporterDescription
	 * @throws IllegalArgumentException
	 */
	public Event(String dataType, String resourceType, Date timestamp, String resourceID, String reporterID, Map<String, String> resourceDescription, Map<String, String> dataDefinition, Map<String, String> reporterDescription) throws IllegalArgumentException {
		validate(dataType, resourceType, timestamp, resourceID, reporterID, resourceDescription, dataDefinition, reporterDescription);

		this.dataType = dataType;
		this.resourceType = resourceType;
		this.timepartition = truncateToMin(timestamp);
		this.timestamp = timestamp;
		this.resourceID = resourceID;
		this.reporterID = reporterID;
		this.resourceDescription = resourceDescription;
		this.dataDefinition = dataDefinition;
		this.reporterDescription = reporterDescription;
		this.uuid = generateUUIDv1();
	}

	/**
	 * Validates this class's constructors arguments, throws an IllegalArgumentException if any argument does not comply with its format
	 *
	 * @param dataType
	 * @param resourceType
	 * @param timestamp
	 * @param resourceID
	 * @param reporterID
	 * @param resourceDescription
	 * @param dataDefinition
	 * @param reporterDescription
	 */
	private void validate(String dataType, String resourceType, Date timestamp, String resourceID, String reporterID, Map<String, String> resourceDescription, Map<String, String> dataDefinition, Map<String, String> reporterDescription) throws IllegalArgumentException {
		if (dataType == null)
			throw new IllegalArgumentException();

		if (resourceType == null)
			throw new IllegalArgumentException();

		if (timestamp == null)
			throw new IllegalArgumentException();

		if (resourceID == null)
			throw new IllegalArgumentException();

		if (reporterID == null)
			throw new IllegalArgumentException();

		if (resourceDescription == null)
			throw new IllegalArgumentException();

		if (dataDefinition == null)
			throw new IllegalArgumentException();

		if (reporterDescription == null)
			throw new IllegalArgumentException();
	}

	/**
	 * Generates a version 1 UUID
	 *
	 * @return uuid
	 */
	private static UUID generateUUIDv1() {
		return Generators.timeBasedGenerator().generate();
	}

	/**
	 * Truncates the date of Date object to its minutes (sets seconds and milliseconds to 0)
	 *
	 * @param timestamp
	 * @return
	 */
	private static Date truncateToMin(Date timestamp) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(timestamp);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	/*	#######
		Setters and Getters
		#######
	 */

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Date getTimepartition() {
		return timepartition;
	}

	public void setTimepartition(Date timepartition) {
		this.timepartition = timepartition;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getResourceID() {
		return resourceID;
	}

	public void setResourceID(String resourceID) {
		this.resourceID = resourceID;
	}

	public String getReporterID() {
		return reporterID;
	}

	public void setReporterID(String reporterID) {
		this.reporterID = reporterID;
	}

	public Map<String, String> getResourceDescription() {
		return resourceDescription;
	}

	public void setResourceDescription(Map<String, String> resourceDescription) {
		this.resourceDescription = resourceDescription;
	}

	public Map<String, String> getDataDefinition() {
		return dataDefinition;
	}

	public void setDataDefinition(Map<String, String> dataDefinition) {
		this.dataDefinition = dataDefinition;
	}

	public Map<String, String> getReporterDescription() {
		return reporterDescription;
	}

	public void setReporterDescription(Map<String, String> reporterDescription) {
		this.reporterDescription = reporterDescription;
	}

	/**
	 * Set the tablename where this model should be applied to.
	 * Please note that this a bit of a hack and should be used carefully,
	 * it should be used before objects of this class are created and not in runtime.
	 *
	 * @param tablename
	 */
	public static void setTable(String tablename) {
		try {
			Annotation originalAnnotation = Event.class.getDeclaredAnnotation(Table.class);

			Object handler = Proxy.getInvocationHandler(originalAnnotation);

			Field f = handler.getClass().getDeclaredField("memberValues");
			f.setAccessible(true);
			Map<String, Object> memberValues = (Map<String, Object>) f.get(handler);

			memberValues.put("name", tablename);

		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof Event) {
			return this.getUuid().compareTo(((Event) o).getUuid());
		}
		return -1;
	}
}
