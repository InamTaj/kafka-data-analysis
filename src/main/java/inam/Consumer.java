package inam;

import com.google.common.io.Resources;
import inam.models.SensorInput;
import inam.utils.ModelUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class Consumer {
	public static void main(String[] args) throws IOException {
		 consumerProcessing();
	}

	public static void consumerProcessing() throws IOException {
		KafkaConsumer<String, String> consumer;
		String propsFile = "consumer.props";
		String topicName = "inamTopic";

		try (InputStream props = Resources.getResource(propsFile).openStream()) {
			Properties properties = new Properties();
			properties.load(props);
			consumer = new KafkaConsumer<>(properties);
		}
		consumer.subscribe(Arrays.asList(topicName));
		int timeouts = 200;
		while (true) {
			// read records with a short timeout.
			ConsumerRecords<String, String> records = consumer.poll(timeouts);
			for (ConsumerRecord<String, String> record : records) {
				// process record
				SensorInput sensorInput = ModelUtils.parseStringToSensorInputModel(
						record.value().toString(),
						ModelUtils.MessageType.INCOMING
				);
				// console output
				System.out.println(sensorInput);
			}
		}
	}

	public static void streamProcessing() throws IOException {
		String propsFile = "consumer.props";
		String topicName = "inamTopic";
		String outputTopicName = "inamOutputTopic";

		try (InputStream props = Resources.getResource(propsFile).openStream()) {
			Properties properties = new Properties();
			properties.load(props);
			final KStreamBuilder builder = new KStreamBuilder();
			final KStream<String, String> textLines = builder.stream(topicName);

			//        final KTable<String, String> wordCounts = textLines.;

			textLines.to(outputTopicName);

			final KafkaStreams streams = new KafkaStreams(builder, properties);

			streams.cleanUp();
			streams.start();
		}
	}
}
