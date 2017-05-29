package inam;

import com.google.common.io.Resources;
import inam.models.SensorDataClass;
import inam.models.SensorInput;
import inam.models.SensorOutput;
import inam.singletons.FileWriterSingleton;
import inam.singletons.SecondConsumerSingleton;
import inam.singletons.SecondProducerSingleton;
import inam.utils.ModelUtils;
import inam.utils.TimeUtils;
import inam.utils.Utils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

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
					calculateSensorRunningCostFromTopic2();
					break;
				default:
					printInvalidArgsError();
			}
		}
	}

	private static void readFromTopic1ViaConsumerRecordsAndTransformData() throws IOException {
		KafkaConsumer<String, String> consumer;

		try (InputStream props = Resources.getResource(Utils.consumerProps).openStream()) {
			Properties properties = new Properties();
			properties.load(props);
			consumer = new KafkaConsumer<>(properties);
		}
		consumer.subscribe(Arrays.asList(Utils.TOPIC_ONE));

		// variables for processing
		SensorInput sensorInput;
		SensorOutput sensorOutput = null;

		while (true) {
			// read records with a short timeout.
			ConsumerRecords<String, String> records = consumer.poll(200);
			for (ConsumerRecord<String, String> record : records) {
				// process records
				// step 1: read data from topic
				sensorInput = ModelUtils.convertStringReadFromTopicToSensorInputModel(record.value());
				// console output
				System.out.println("read from topic " + Utils.TOPIC_ONE + ": " + sensorInput);

				// step 2: transform it to SensorOutputModel
				sensorOutput = ModelUtils.parseSensorInputToSensorOutput(sensorInput);

				System.out.println("writing transformed data: " + sensorOutput + " to file & topic: " + Utils.TOPIC_TWO);
				// step 3: write SensorOutputModel to 2nd kafka topic
				writeDataToSecondKafkaTopic(sensorOutput);

				// step 4: write SensorOutputModel to a physical file
				writeOutputModelToFile(sensorOutput);
			}
		}
	}

	private static void writeDataToSecondKafkaTopic(SensorOutput sensorOutput) {
		ProducerRecord<String, String> record = new ProducerRecord<>(Utils.TOPIC_TWO, sensorOutput.toString());
		org.apache.kafka.clients.producer.Producer<String, String> producer = SecondProducerSingleton.getInstance();
		producer.send(record);
	}

	private static void writeOutputModelToFile(SensorOutput sensorOutput) {
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = FileWriterSingleton.getInstance(Utils.TRANSFORMED_DATA_FILE);
			bufferedWriter.write(sensorOutput.toString());
			bufferedWriter.newLine();
		}
		catch(IOException ex) {
			System.out.println("Exception occurred while writing transformed data to file...");
		}
		finally {
			try {bufferedWriter.close();}
			catch (IOException ex) {}
		}
	}

	/**
	 * STRATEGY
	 *
	 * Consume:
	 *      + read from Topic line by line and parse it as Formatted Data POJO (SensorOutput)
	 *
	 * Process (Part 1):
	 *      + for each SensorOutput record, create a DataClass object
	 *      + DataClass object that will store following variables of SensorOutput:
	 *          - sum of all powers
	 *          - lowestTimeStamp and highestTimeStamp (will be used later to calculate total running time of sensor)
	 *      + store this object in HashMap -> KEY = sensorId || VALUE = DataClass object
	 *      + finally HashMap will contain a DataClass object for each sensorId with total power calculated and time values
	 *
	 * Process (Part 2):
	 *      + process HashMap to calculate running Time for each sensor
	 *      + calculate sensor running cost for each hour
	 *      + write all of this data (sensorId, power, sensor-running-cost) in a new file
	 */
	private static void calculateSensorRunningCostFromTopic2() {
		TimeUtils timeUtils = new TimeUtils();
		KafkaConsumer<String, String> consumer = SecondConsumerSingleton.getInstance();
		final HashMap<Integer, SensorDataClass> mapOfSensors = new HashMap<>();
		SensorOutput sensorOutput;
		SensorDataClass pojo;
		String shouldContinue;
		do {

			// read records with a short timeout.
			ConsumerRecords<String, String> records = consumer.poll(200);
			for (ConsumerRecord<String, String> record : records) {
				sensorOutput = ModelUtils.convertStringReadFromTopicToSensorOutputModel(record.value());
				System.out.println("read from topic #2: " + sensorOutput);

				int sensorId = sensorOutput.getId();

				// search pojo against ID of sensor in the MAP
				pojo = mapOfSensors.get(sensorId);

				if (pojo == null) {
					pojo = new SensorDataClass(sensorId);
				}

				// calculate sum of power
				float[] previousPower = pojo.getPower();
				float[] currentPower = sensorOutput.getPower();
				float[] newPower = new float[3];
				for (int i = 0; i < previousPower.length; i++) {
					newPower[i] = previousPower[i] + currentPower[i];
				}
				pojo.setPower(newPower);        // set new power vaues
				System.out.println("calculating new power values......done");

				// set lowest and highest times
				String currentTime = sensorOutput.getTime();
				String previousLowTime = pojo.getTimeLowest();
				String previousHighTime = pojo.getTimeHighest();
				pojo.setTimeLowest(timeUtils.getSmallestDateTime(currentTime, previousLowTime));
				pojo.setTimeHighest(timeUtils.getLargestDateTime(currentTime, previousHighTime));
				System.out.println("calculating new dateTime values......done");

				// replacing pojo with new values
				mapOfSensors.put(Integer.valueOf(sensorId), pojo);
			}

			mapOfSensors.forEach((integer, sensorData) -> {
				// perform final calculations on mapOfSensors

				// 1- get total running time for sensor
				double hours = timeUtils.getTotalHoursBetweenTwoDateTimes(sensorData.getTimeLowest(), sensorData.getTimeHighest());
				sensorData.setTotalRunningHours(hours);

				// 2- get cost per hour
				sensorData.setCostPerHour( getRunningCostPerHour(sensorData.getPower(), hours) );

				// 3- write final sensorDataObjects from mapOfSensors on file
				writeSensorDataToClass(sensorData);

				System.out.println("final sensor-data with computed values: " + sensorData);
			});

			System.out.println("\n\n>>> Should I run again for all  (new + old) records? (y/n) (yes/no)");
			shouldContinue = new Scanner(System.in).next();
		}
		while (shouldContinue.equalsIgnoreCase("y") || shouldContinue.equalsIgnoreCase("yes"));
	}

	private static double getRunningCostPerHour(float[] power, double hours) {
		float totalPower = 0;
		for(float i: power) {
			totalPower += i;
		}
		return ((totalPower * hours) / 60) * 15;
	}

	private static void writeSensorDataToClass(SensorDataClass sensorData) {
		System.out.print("Writing computed sensor data to file..");
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = FileWriterSingleton.getInstance(Utils.FINAL_COST_PER_HOUR);
			bufferedWriter.write(sensorData.toString());
			bufferedWriter.newLine();
			System.out.println("......done");
		}
		catch(IOException ex) {
			System.out.println("Exception occurred while writing sensor data to file...");
		}
		finally {
			try {bufferedWriter.close();}
			catch (IOException ex) {}
		}
	}

	private static void printInvalidArgsError() {
		System.err.println("Invalid argument after \"consumer\". Please type any of the following options: \nusage: consumer ");
		System.out.println("\t[--transform-data]\n\t[--sensor-cost]");
	}
}
