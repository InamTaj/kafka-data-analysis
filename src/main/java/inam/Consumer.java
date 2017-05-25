package inam;

import com.google.common.io.Resources;
import inam.models.SensorInput;
import inam.models.SensorOutput;
import inam.producers.SecondProducer;
import inam.utils.ModelUtils;
import inam.utils.Utils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class Consumer {
	public static void main(String[] args) throws IOException {
		String operation = "";
		try { operation = args[1]; }
		catch (ArrayIndexOutOfBoundsException ex) { printInvalidArgsError(); }
		if (!operation.isEmpty() || operation.length() > 0) {
			switch (operation) {
				case "--transform-data":
					readFromTopic1ViaConsumerRecordsAndTransformData();
					break;
				case "--sensor-cost":
					System.out.println("costing it...");
					break;
				default:
					printInvalidArgsError();
			}
		}
	}

	public static void readFromTopic1ViaConsumerRecordsAndTransformData() throws IOException {
		KafkaConsumer<String, String> consumer;
		String propsFile = "consumer.props";

		try (InputStream props = Resources.getResource(propsFile).openStream()) {
			Properties properties = new Properties();
			properties.load(props);
			consumer = new KafkaConsumer<>(properties);
		}
		consumer.subscribe(Arrays.asList(Utils.TOPIC_ONE));
		int timeouts = 200;
		// variables for processing
		SensorInput sensorInput;
		SensorOutput sensorOutput = null;

		while (true) {
			// read records with a short timeout.
			ConsumerRecords<String, String> records = consumer.poll(timeouts);
			for (ConsumerRecord<String, String> record : records) {
				// process records
				// step 1: read data from topic
				sensorInput = ModelUtils.parseStringToSensorInputModel(
						record.value().toString(),
						ModelUtils.MessageType.INCOMING
				);
				// console output
				System.out.println(sensorInput);

				// step 2: transform it to SensorOutputModel
				sensorOutput = ModelUtils.parseSensorInputToSensorOutput(sensorInput);

				// step 3: write SensorOutputModel to 2nd kafka topic
				writeDataToSecondKafkaTopic(sensorOutput);

				// step 4: TODO: write SensorOutputModel to a physical file

			}
		}
	}

	private static void writeDataToSecondKafkaTopic(SensorOutput sensorOutput) {
		System.out.print("Writing transformed data to second topic: " + Utils.TOPIC_TWO);

		ProducerRecord<String, String> record = new ProducerRecord<>(Utils.TOPIC_TWO, sensorOutput.toString());
		org.apache.kafka.clients.producer.Producer<String, String> producer = SecondProducer.getInstance();
		producer.send(record);

		System.out.println("......done");
	}

	private static void writeOutputModelToFile(SensorOutput sensorOutput) {
		System.out.print("Writing transformed data to file..");
		System.out.println("......done");
	}

	public static void readFromTopic1ViaStreamAndTransformData() throws IOException {
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

	private static void printInvalidArgsError() {
		System.err.println("Invalid argument after \"consumer\". Please type any of the following options: \nusage: consumer ");
		System.out.println("\t[--transform-data]\n\t[--sensor-cost]");
	}
}
