# Raw Data Loader (RDL)
This componenent is designed to consume JSON data records that are published into a Kafka message bus and to transform them in such a way that they can be inserted into a Cassandra DB.
It features multi-threaded Kafka consumers and Cassandra persisters.

Please note that, at this stage, the current RDL version might lose Kafka records from the moment they are consumed until they are persisted!

## Requirements
* Maven version 3.3.9 (for compilation)
* Java 8

## Compile
```bash
mvn clean package
```

## Configure
Configurations are done via an yaml file that is loaded into RDL.

## Run
With built-in configurations:
```bash
java -jar <path-to-jar>
```

Using a configuration file:
```bash
java -jar <path-to-jar> -c configuration.yaml
```

Note that we already provide a sample configuration file in the resources directory.

## Interfaces
### Input
RDL input is a list of kafka topics to where json files are published.
The json format can be found below:
```javascrypt
{
	"Data":
	[
		{
			"timestamp":	Long,		//report time in epoch milliseconds
			"dataType":	String,		//type of data being reported -> [statistics, event, alarm] 
			"reporterID":	String,		//FDN, reporter unique identifier built using fields and values from reporterDescription
			"resourceType": String,		//type of resource being reported -> [flow, vm_disk, dpi, ...]
			"resourceID": 	String,		//FDN, resource unique identifier built using fields and values from resourceDescription
			"resourceDescription":		//resource attributes in a string to string map
			{
				"key1": 	String,
				"key2": 	String,
				...
				"keyN": 	String
			},
			"dataDefinition":		//data values/definition in a string to double map if dataType == statistics or in a string to string if dataType != statistics
			{
				"severity":	Int,	//mandatory field when dataType == alarm
				"key1":		value,
				"key2": 	value,
				...
				"keyN": 	value
			},
			"reporterDescription":		//reporter attributes in a string to string map
			{
				"key1": 	String,
				"key2": 	String,
				...
				"keyN": 	String
			}
		}
	]
}

```
The model here can be used to represent reports about to statistical counters, events and alarms.

### Output
RDL outputs the consumed jsons into two Cassandra tables: one for statistical counters and another for events/alarms.
The Cassandra DDL can be found here:
```cql
--as dba:
grant ALL on keyspace selfnet to selfnet;
grant SELECT on keyspace system to selfnet;
grant SELECT on keyspace system_traces to selfnet;

--as user selfnet, on keyspace selfnet:
--counters table
create table selfnet_counters
(
	timestamp		timestamp,
	timepartition		timestamp,
	resourceType		text,
	counterType 		text,
	resourceId 		text,
	reporterId 		text,
	resourceDescription	map<text,text>,
	dataDefinition		map<text,double>,
	reporterDescription 	map<text,text>,
	PRIMARY KEY ((resourceType,timepartition), timestamp, counterType, resourceId, reporterId)
);

--events/alarms table
create table selfnet_events
(
	timestamp 		timestamp,
	timepartition 		timestamp,
	resourceType 		text,
	dataType 		text,
	resourceId 		text,
	reporterId 		text,
	resourceDescription 	map<text,text>,
	dataDefinition	 	map<text,text>,
	reporterDescription 	map<text,text>,
	PRIMARY KEY ((dataType,resourceType,timepartition), timestamp, resourceId, reporterId)
);
```


#### Cassandra table models:
##### Counters
<table>
  <tr>
    <th rowspan="2">timestamp<br>(timestamp)</th>
    <th rowspan="2">timepartition<br>(timestamp)</th>
    <th rowspan="2">resourceType<br>(text)<br></th>
    <th rowspan="2">counterType<br>(text)</th>
    <th rowspan="2">resourceId<br>(text)</th>
    <th rowspan="2">reporterId<br>(text)</th>
    <th colspan="2">resourceDescription</th>
    <th colspan="2">dataDefinition<br></th>
    <th colspan="2">reporterDescription</th>
  </tr>
  <tr>
    <td>Name<br>(text)<br></td>
    <td>Value<br>(text)<br></td>
    <td>Name<br>(text)<br></td>
    <td>Value<br>(double)<br></td>
    <td>Name<br>(text)<br></td>
    <td>Value<br>(text)</td>
  </tr>
</table>

##### Events
<table>
  <tr>
    <th rowspan="2">timestamp<br>(timestamp)</th>
    <th rowspan="2">timepartition<br>(timestamp)</th>
    <th rowspan="2">resourceType<br>(text)<br></th>
    <th rowspan="2">dataType<br>(text)</th>
    <th rowspan="2">resourceId<br>(text)</th>
    <th rowspan="2">reporterId<br>(text)</th>
    <th colspan="2">resourceDescription</th>
    <th colspan="2">dataDefinition<br></th>
    <th colspan="2">reporterDescription</th>
  </tr>
  <tr>
    <td>Name<br>(text)<br></td>
    <td>Value<br>(text)<br></td>
    <td>Name<br>(text)<br></td>
    <td>Value<br>(text)<br></td>
    <td>Name<br>(text)<br></td>
    <td>Value<br>(text)</td>
  </tr>
</table>
