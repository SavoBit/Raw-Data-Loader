package pt.ptinovacao.selfnet.aggregation.util;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class MyKafkaProducer {
	private final Properties config = new Properties();

	private final Producer<String, String> producer;

	public MyKafkaProducer(String bootstrapServers) {
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

       /* -- Other configurations
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        config.put(ProducerConfig.CLIENT_ID_CONFIG, id);
        config.put(ProducerConfig.METADATA_MAX_AGE_CONFIG, Long.toString(maxAge));
        config.put(ProducerConfig.SEND_BUFFER_CONFIG, Integer.toString(size));
        config.put(ProducerConfig.RECEIVE_BUFFER_CONFIG, Integer.toString(size));
        config.put(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, Integer.toString(interval));
        config.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, Integer.toString(interval));
        config.put(ProducerConfig.METRICS_NUM_SAMPLES_CONFIG, Integer.toString(samples));
        config.put(ProducerConfig.METRICS_SAMPLE_WINDOW_MS_CONFIG, Integer.toString(window));
        config.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, Integer.toString(time));
        config.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, Integer.toString(timeout));
        config.put(ProducerConfig.METADATA_MAX_AGE_CONFIG, Integer.toString(maxAge));

        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, cls);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.common.kafka.common.value.serializer");
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(size));
        config.put(ProducerConfig.LINGER_MS_CONFIG, Long.toString(ms));
        config.put(ProducerConfig.ACKS_CONFIG, Integer.toString(number));
        config.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, Integer.toString(size));
        config.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, Long.toString(ms));
        config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, Long.toString(memory));
        config.put(ProducerConfig.RETRIES_CONFIG, Integer.toString(number));
        config.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, cType.toString());
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, Integer.toString(max));
        */

		producer = new KafkaProducer<String, String>(config);
	}

	//Send
	public Future<RecordMetadata> send(ProducerRecord<String, String> record, Callback callback) {
		return producer.send(record, callback);
	}

	public Future<RecordMetadata> send(ProducerRecord<String, String> record) {
		return producer.send(record);
	}

	public Future<RecordMetadata> send(String topic, String key, String message, Callback callback) {
		return send(new ProducerRecord<String, String>(topic, key, message), callback);
	}

	public Future<RecordMetadata> send(String topic, String key, String message) {
		return send(new ProducerRecord<String, String>(topic, key, message));
	}


	//close
	public void close() {
		producer.close();
	}

	public void close(long secondsTimeout) {
		producer.close(secondsTimeout, TimeUnit.SECONDS);
	}


	public void flush()
	{
		producer.flush();
	}
	/* - Just a small test - */
	public void TestSend(int messageCount, String topic, String textToSend) {
		for (int i = 0; i < messageCount; i++)
			send(topic, "id_" + i, textToSend);
	}
}
