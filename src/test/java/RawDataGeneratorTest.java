/**
 * @author rui-d-pedro
 */
public class RawDataGeneratorTest
{
	public static void main(String... args)
	{
		System.out.println(genFMA());
	}

	public static String genFMA()
	{
		String ts = String.valueOf(System.currentTimeMillis());

		return "{\n" +
				"\t\"Data\": [{\n" +
				"\t\t\"timestamp\": "+ts+",\n" +
				"\t\t\"dataType\": \"statistics\",\n" +
				"\t\t\"reporterID\": \"reporterHostName=ComputeA/reporterIP=192.168.0.1/reporterAppType=FMA\",\n" +
				"\t\t\"resourceType\": \"FLOW\",\n" +
				"\t\t\"resourceID\": \"flowLayer=3/flowHash=45FA6E20/previousflowHash=6D76F8A6/l3Proto=null/srcIP=192.188.0.140/destIP=192.188.0.2/l4Proto=17/flowLabel=null/srcPort=5004/destPort=5004/l7Proto=rtp/l7Key1=96/l7Key2=0/l7Key3=1/l7Key4=null/encapsulationLayer=2/encapsulationID1=00000445/encapsulationID2=8894D0D4/encapsulationID3=null/encapsulationID4=null/encapsulationID5=null\",\n" +
				"\t\t\"resourceDescription\": {\n" +
				"\t\t\t\"flowLayer\": 3,\n" +
				"\t\t\t\"flowHash\": \"45FA6E20\",\n" +
				"\t\t\t\"previousflowHash\": \"6D76F8A6\",\n" +
				"\t\t\t\"encapsulationID1\": \"00000445\",\n" +
				"\t\t\t\"encapsulationID2\": \"8894D0D4\",\n" +
				"\t\t\t\"encapsulationID3\": null,\n" +
				"\t\t\t\"encapsulationID4\": null,\n" +
				"\t\t\t\"encapsulationID5\": null,\n" +
				"\t\t\t\"encapsulationType1\": \"vxlan\",\n" +
				"\t\t\t\"encapsulationType2\": \"gtp\",\n" +
				"\t\t\t\"encapsulationType3\": null,\n" +
				"\t\t\t\"encapsulationType4\": null,\n" +
				"\t\t\t\"encapsulationType5\": null,\n" +
				"\t\t\t\"encapsulationLayer\": 2,\n" +
				"\t\t\t\"macSrc\": null,\n" +
				"\t\t\t\"macDst\": null,\n" +
				"\t\t\t\"l3Proto\": null,\n" +
				"\t\t\t\"srcIP\": \"192.188.0.140\",\n" +
				"\t\t\t\"destIP\": \"192.188.0.2\",\n" +
				"\t\t\t\"l4Proto\": \"17\",\n" +
				"\t\t\t\"flowLabel\": null,\n" +
				"\t\t\t\"srcPort\": \"5004\",\n" +
				"\t\t\t\"destPort\": \"5004\",\n" +
				"\t\t\t\"l7Proto\": \"rtp\",\n" +
				"\t\t\t\"l7Key1\": \"96\",\n" +
				"\t\t\t\"l7Key2\": \"0\",\n" +
				"\t\t\t\"l7Key3\": \"1\",\n" +
				"\t\t\t\"l7Key4\": null,\n" +
				"\t\t\t\"firstSeenTos\": \"0\",\n" +
				"\t\t\t\"autonomousSystemId\": 0,\n" +
				"\t\t\t\"firstPacketSeen\": 1491486587000,\n" +
				"\t\t\t\"packetStructure\": \"/rtp\",\n" +
				"\t\t\t\"completePacketStructure\": \"/mac:14/ip4:20/udp:8/vxlan:8/mac:14/ip4:20/udp:8/gtp:8/ip4:20/udp:8\",\n" +
				"\t\t\t\"temporalLayerID\": \"1\",\n" +
				"\t\t\t\"tos\": \"0\",\n" +
				"\t\t\t\"flowState\": \"ACTIVE\",\n" +
				"\t\t\t\"payloadType\": \"96\",\n" +
				"\t\t\t\"layerID\": \"0\"\n" +
				"\t\t},\n" +
				"\t\t\"dataDefinition\": {\n" +
				"\t\t\t\"lastInterPacketRate\": 154858,\n" +
				"\t\t\t\"sumInterPacketLagNS\": 1858394003,\n" +
				"\t\t\t\"timeLastPacketReceivedMs\": 1491486587000,\n" +
				"\t\t\t\"currentOctetsPerPeriod\": 360932,\n" +
				"\t\t\t\"totalOctets\": 353125,\n" +
				"\t\t\t\"currentPktPerPeriod\": 382,\n" +
				"\t\t\t\"currentSumInterPacketLagNSPerPeriod\": 1802414713,\n" +
				"\t\t\t\"totalOctetsOutter\": 461718,\n" +
				"\t\t\t\"totalpktCount\": 386,\n" +
				"\t\t\t\"currentOutterOctetsPerPeriod\": 461720,\n" +
				"\t\t\t\"timeLastPacketReceived\": 66270761084109\n" +
				"\t\t},\n" +
				"\t\t\"reporterDescription\": {\n" +
				"\t\t\t\"reporterHostName\": \"ComputeA\",\n" +
				"\t\t\t\"reporterIP\": \"192.168.0.1\",\n" +
				"\t\t\t\"reporterEpochTime\": \"1491486587000\",\n" +
				"\t\t\t\"reporterAppType\": \"FMA\",\n" +
				"\t\t\t\"reporterMAC\": \"b3:3b:a2:41:ae:84\"\n" +
				"\t\t}\n" +
				"\t}]\n" +
				"}";
	}

	public static String genSNORT()
	{
		String ts = String.valueOf(System.currentTimeMillis());

		return "{\n" +
				"\t\"Data\": [{\n" +
				"\t\t\"timestamp\": "+ts+",\n" +
				"\t\t\"dataType\": \"event\",\n" +
				"\t\t\"reporterID\": \"reporterHostName=SnortAgent/reporterIP=193.136.93.101/reporterAppType=SMA\",\n" +
				"\t\t\"resourceType\": \"snort-dpi-test\",\n" +
				"\t\t\"resourceID\": \"sensorId=0/eventIdTimestamp=1490615204444/signatureId=10000001/generatorId=1\",\n" +
				"\t\t\"resourceDescription\": {\n" +
				"\t\t\t\"sensorId\": \"0\",\n" +
				"\t\t\t\"signatureId\": \"10000001\",\n" +
				"\t\t\t\"generatorId\": \"1\",\n" +
				"\t\t\t\"signatureRevision\": \"1\",\n" +
				"\t\t\t\"classificationId\": \"31\"\n" +
				"\t\t},\n" +
				"\t\t\"dataDefinition\": {\n" +
				"\t\t\t\"srcIP\": \"155.54.205.2\",\n" +
				"\t\t\t\"dstIP\": \"155.54.205.4\",\n" +
				"\t\t\t\"dstServiceName\": \"zeus_cc_server\",\n" +
				"\t\t\t\"classification\": \"zombie_detected\"\n" +
				"\t\t},\n" +
				"\t\t\"reporterDescription\": {\n" +
				"\t\t\t\"reporterHostname\": \"SnortAgent\",\n" +
				"\t\t\t\"reporterIP\": \"193.136.93.101\",\n" +
				"\t\t\t\"reporterEpochTime\": \"1491487846000\",\n" +
				"\t\t\t\"reporterAppType\": \"DPI\",\n" +
				"\t\t\t\"reporterMAC\": \"00:ff:12:34:56:34\"\n" +
				"\t\t}\n" +
				"\t}]\n" +
				"}";
	}

	public static String genTest()
	{
		String ts = String.valueOf(System.currentTimeMillis());

		return "{\n" +
				"  \"Data\": [\n" +
				"    {\n" +
				"      \"timestamp\": "+ts+",\n" +
				"      \"dataType\": \"alarm\",\n" +
				"      \"reporterID\": \"reporterHostName=SnortAgent/reporterIP=193.136.93.101/reporterTenant=null/reporterAppType=SMA\",\n" +
				"      \"resourceType\": \"flow\",\n" +
				"      \"resourceID\": \"flowLayer=0/flowHash=7D057306/previousflowHash=null/l3Proto=2048/srcIP=146.191.50.80/destIP=146.191.241.166/l4Proto=1/flowLabel=null/srcPort=null/destPort=null/l7Proto=null/l7Key1=null/l7Key2=null/l7Key3=null/l7Key4=null/encapsulationLayer=0/encapsulationID1=null/encapsulationID2=null/encapsulationID3=null\",\n" +
				"      \"resourceDescription\": {\n" +
				"        \"flowLayer\": 0,\n" +
				"        \"flowHash\": \"7D057306\",\n" +
				"        \"macSrc\": \"52:54:00:FC:A9:F6\",\n" +
				"        \"macDst\": \"00:21:D7:6A:E4:00\",\n" +
				"        \"l3Proto\": 2048,\n" +
				"        \"srcIP\": \"146.191.50.80\",\n" +
				"        \"destIP\": \"146.191.241.166\",\n" +
				"        \"outterSrcIP\": \"146.191.50.80\",\n" +
				"        \"outterDestIP\": \"146.191.241.166\",\n" +
				"        \"l4Proto\": 1,\n" +
				"        \"firstSeenTos\": \"0\",\n" +
				"        \"encapsulationLayer\": 0,\n" +
				"        \"autonomousSystemId\": 0,\n" +
				"        \"packetStructure\": \"/mac:14/ip4:20\",\n" +
				"        \"completePacketStructure\": \"\",\n" +
				"        \"firstPacketSeen\": 1492792264509\n" +
				"      },\n" +
				"      \"dataDefinition\": {\n" +
				"        \"severity\": 3,\n" +
				"        \"sensorId\": 0,\n" +
				"        \"eventId\": 51,\n" +
				"        \"eventSecond\": 1492790266,\n" +
				"        \"eventMicrosecond\": 883330,\n" +
				"        \"signatureId\": 10000001,\n" +
				"        \"generatorId\": 1,\n" +
				"        \"signatureRevision\": 1,\n" +
				"        \"classificationId\": 31,\n" +
				"        \"ipSource\": 2462003792,\n" +
				"        \"ipDestination\": 2462052774,\n" +
				"        \"sPortItype\": 524288,\n" +
				"        \"dPortIcode\": 0,\n" +
				"        \"protocol\": 1,\n" +
				"        \"packetAction\": 0,\n" +
				"        \"pad\": 0,\n" +
				"        \"packetSecond\": 1492790266,\n" +
				"        \"packetMicrosecond\": 883330,\n" +
				"        \"linktype\": 1,\n" +
				"        \"packetLength\": 98,\n" +
				"        \"flowHash\": \"7D057306\",\n" +
				"        \"abstractionLayer\": 0\n" +
				"      },\n" +
				"      \"reporterDescription\": {\n" +
				"        \"reporterHostName\": \"SnortAgent\",\n" +
				"        \"reporterIP\": \"193.136.93.101\",\n" +
				"        \"reporterTenant\": \"null\",\n" +
				"        \"reporterAppType\": \"SMA\",\n" +
				"        \"reportEpochTime\": 1492792264510\n" +
				"      }\n" +
				"    }\n" +
				"  ]\n" +
				"}\n";
	}
}
