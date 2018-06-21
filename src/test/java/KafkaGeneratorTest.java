import pt.ptinovacao.selfnet.aggregation.util.MyKafkaProducer;

/**
 * Created by Force on 28/04/2017.
 */
public class KafkaGeneratorTest {
	public static void main(String... args){
		String topic = "test2";
		String bootstrapserver = "192.168.89.226:9092";
		MyKafkaProducer producer = new MyKafkaProducer(bootstrapserver);

		String jsonEvent = RawDataGeneratorTest.genSNORT();

		producer.send(topic, "", jsonEvent);
		producer.flush();
		producer.close();
	}
}
