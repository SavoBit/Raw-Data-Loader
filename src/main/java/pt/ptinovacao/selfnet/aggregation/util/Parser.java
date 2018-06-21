package pt.ptinovacao.selfnet.aggregation.util;

import com.google.common.math.DoubleMath;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ptinovacao.selfnet.aggregation.models.BaseModel;
import pt.ptinovacao.selfnet.aggregation.models.Counter;
import pt.ptinovacao.selfnet.aggregation.models.Event;

import java.math.RoundingMode;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * TODO - refine logging and exceptions handling
 *
 * @author rui-d-pedro
 */
public class Parser {
	//Logger
	private static final Logger LOGGER = LoggerFactory.getLogger(Parser.class);

	//String to Json Object parser
	private static final Gson gson = new Gson();

	private static final String regexOuterQuotesRemoval = "(^[\"'])|([\"']$)";

	private static final String defaultJsonStringValue = "";
	private static final Double defaultJsonDoubleValue = Double.MAX_VALUE;

	/**
	 * TODO - implement
	 *
	 * @param json
	 * @return
	 */
	private static boolean validate(JsonObject json) {
		return true;
	}

	/**
	 * Parse FMA Json to Raw Data Model
	 *
	 * @param jsonString
	 * @return
	 */
	@Deprecated
	public static ConcurrentSkipListSet<Counter> parseFMA(String jsonString) {

		ConcurrentSkipListSet<Counter> models = new ConcurrentSkipListSet<>();

		//parse the json string record into an actual JsonObject
		JsonObject json = gson.fromJson(jsonString, JsonObject.class);

		//validate the json before parsing/mapping it
		if (validate(json)) {

			/*
			 * (the following parsing actions only support the old FMA json format)
			 */

			//Retrieve the Flows array and iterate over it
			if (json.has("Flows"))
				json.get("Flows").getAsJsonArray().getAsJsonArray().forEach(element ->
				{
					//Retrieve the main object containing the flow
					JsonObject object = element.getAsJsonObject();
					//Retrieve FlowEntry
					JsonObject flowEntry = object.getAsJsonObject("FlowEntry");
					//Retrieve Statistics
					JsonObject stats = object.getAsJsonObject("Statistics");

					//assuming that resourceType = "FLOW"
					String resourceType = "FLOW";

					//build resourceDescription
					Map<String, String> resourceDescription = new ConcurrentHashMap<>();
					flowEntry.entrySet().forEach(entry -> resourceDescription.put(entry.getKey(), entry.getValue().getAsString()));

					//build resourceStatistics
					Map<String, Double> resourceStatistics = new ConcurrentHashMap<>();
					stats.entrySet().forEach(entry -> resourceStatistics.put(entry.getKey(), entry.getValue().getAsDouble()));

					//convert the timestamp back to long since it was cast to Double when building the resourceStatistics map
					Date timestamp = new Date(DoubleMath.roundToLong(resourceStatistics.get("firstSeen"), RoundingMode.UNNECESSARY));
					//assuming that the resourceID is the FMA ReporterName
					String resourceID = json.get("ReporterName").toString();

					String counterType;
					if (resourceStatistics.size() == 1)
						counterType = resourceStatistics.entrySet().iterator().next().getKey().toString();
					else
						counterType = "MULTI_COUNTER";

//					Counter r = new Counter(resourceType, timestamp, resourceID, counterType, resourceDescription, resourceStatistics);
					//map the json into our RawRawDataModel and save it into Cassandra
//					models.add(r);
				});
		}
		return models;
	}

	/**
	 * Parse JSON raw data model for counters into the Counter class
	 * that reflects the same model within Cassandra
	 * TODO - logging, proper exception handling
	 * TODO - refactor for proper json validation (it is shameful I know)
	 * TODO - enforce the severity field (inside the dataDefinition map) when json is an alarm event
	 *
	 * @param jsonString
	 * @return
	 */
	public static ConcurrentSkipListSet<BaseModel> parse(String jsonString) {
		ConcurrentSkipListSet<BaseModel> models = new ConcurrentSkipListSet<>();

		//parse the json string record into an actual JsonObject
		JsonObject json = gson.fromJson(jsonString, JsonObject.class);

		//validate the json before parsing/mapping it
		if (validate(json)) {
			if (json.has("Data")) {
				json.get("Data").getAsJsonArray().forEach(element ->
				{
					//Retrieve the main object containing the flow
					JsonObject object = element.getAsJsonObject();

					Date timestamp = null;
					String dataType = null;
					String reporterID = null;
					String resourceType = null;
					String resourceID = null;
					Map<String, String> resourceDescription = new ConcurrentHashMap<>();
					Map<String, Double> dataDefinitionCounters = new ConcurrentHashMap<>();
					Map<String, String> dataDefinitionEvents = new ConcurrentHashMap<>();
					Map<String, String> reporterDescription = new ConcurrentHashMap<>();

					if (object.has("timestamp")) {
						//long
						long timestampLong = object.get("timestamp").getAsLong();
						//convert the timestamp back to long since it was cast to Double when building the dataDefinition map
						timestamp = new Date(timestampLong);
					} else {
						LOGGER.error("timestamp field not found.");
					}

					if (object.has("dataType")) {
						dataType = object.get("dataType").getAsString().replaceAll(regexOuterQuotesRemoval, "");
					} else {
						LOGGER.error("dateType field not found");
					}

					if (object.has("reporterID")) {
						//string
						reporterID = object.get("reporterID").getAsString().replaceAll(regexOuterQuotesRemoval, "");
					} else {
						LOGGER.error("reporterID field not found.");
					}

					if (object.has("resourceType")) {
						//string
						resourceType = object.get("resourceType").getAsString().replaceAll(regexOuterQuotesRemoval, "");
					} else {
						LOGGER.error("resourceType field not found.");
					}

					if (object.has("resourceID")) {
						//string
						resourceID = object.get("resourceID").getAsString().replaceAll(regexOuterQuotesRemoval, "");
					} else {
						LOGGER.error("resourceID field not found.");
					}

					if (object.has("resourceDescription")) {
						//build resourceDescription
						object.get("resourceDescription").getAsJsonObject()
							  .entrySet()
							  .parallelStream()
							  .map(entry -> {
								  if (entry.getValue().isJsonNull())
									  entry.setValue(new JsonPrimitive(defaultJsonStringValue));
								  return entry;
							  })
							  .forEach(entry -> resourceDescription.put(entry.getKey(), entry.getValue().getAsString().replaceAll(regexOuterQuotesRemoval, "")));
					} else {
						LOGGER.error("resourceDescription field not found.");
					}

					if (object.has("dataDefinition")) {
						//map
						if (dataType != null)
							switch (dataType.toLowerCase()) {
								case "alarm":
									//build dataDefinition
									object.get("dataDefinition").getAsJsonObject()
										  .entrySet()
										  .parallelStream()
										  .map(entry -> {
											  if (entry.getValue().isJsonNull())
												  entry.setValue(new JsonPrimitive(defaultJsonStringValue));
											  return entry;
										  })
										  .forEach(entry -> dataDefinitionEvents.put(entry.getKey(), entry.getValue().getAsString().replaceAll(regexOuterQuotesRemoval, "")));
									break;
								case "event":
									//build dataDefinition
									object.get("dataDefinition").getAsJsonObject()
										  .entrySet()
										  .parallelStream()
										  .map(entry -> {
											  if (entry.getValue().isJsonNull())
												  entry.setValue(new JsonPrimitive(defaultJsonStringValue));
											  return entry;
										  })
										  .forEach(entry -> dataDefinitionEvents.put(entry.getKey(), entry.getValue().getAsString().replaceAll(regexOuterQuotesRemoval, "")));
									break;
								case "statistics":
									//build dataDefinition
									object.get("dataDefinition").getAsJsonObject()
										  .entrySet()
										  .parallelStream()
										  .map(entry -> {
											  if (entry.getValue().isJsonNull())
												  entry.setValue(new JsonPrimitive(defaultJsonDoubleValue));
											  return entry;
										  })
										  .forEach(entry -> dataDefinitionCounters.put(entry.getKey(), entry.getValue().getAsDouble()));
									break;
								default:
									LOGGER.error("dataType value not recognized, dataDefinition field could not be parsed.");
									break;
							}
						else {
							LOGGER.error("dataType value is null, dataDefinition field could not be parsed.");
						}
					} else {
						LOGGER.error("dataDefinition field not found.");
					}

					if (object.has("reporterDescription")) {
						//map
						//build dataDefinition
						object.get("reporterDescription").getAsJsonObject()
							  .entrySet()
							  .parallelStream()
							  .map(entry -> {
								  if (entry.getValue().isJsonNull())
									  entry.setValue(new JsonPrimitive(defaultJsonStringValue));
								  return entry;
							  })
							  .forEach(entry -> reporterDescription.put(entry.getKey(), entry.getValue().getAsString().replaceAll(regexOuterQuotesRemoval, "")));
					} else {
						LOGGER.error("reporterDescription field not found.");
					}

					if (timestamp != null && dataType != null && resourceType != null && resourceID != null && reporterID != null) {
						String counterType;
						switch (dataType.toLowerCase()) {
							case "alarm": {
								Event event = new Event(dataType, resourceType, timestamp, resourceID, reporterID, resourceDescription, dataDefinitionEvents, reporterDescription);
								//map the json into our Raw and Aggregation Data Model and save it into Cassandra
								models.add(event);
								break;
							}
							case "event": {
								Event event = new Event(dataType, resourceType, timestamp, resourceID, reporterID, resourceDescription, dataDefinitionEvents, reporterDescription);
								//map the json into our Raw and Aggregation Data Model and save it into Cassandra
								models.add(event);
								break;
							}
							case "statistics": {
								if (dataDefinitionCounters.size() == 1)
									counterType = dataDefinitionCounters.entrySet().iterator().next().getKey().toString();
								else
									counterType = "MULTI_COUNTER";
								Counter counter = new Counter(resourceType, timestamp, resourceID, reporterID, counterType, resourceDescription, dataDefinitionCounters, reporterDescription);
								//map the json into our Raw and Aggregation Data Model and save it into Cassandra
								models.add(counter);
								break;
							}
							default:
								break;
						}
					} else {
						LOGGER.error("JSON: {} - could not be parsed.", jsonString);
					}
				});
			}
		}
		return models;
	}
}