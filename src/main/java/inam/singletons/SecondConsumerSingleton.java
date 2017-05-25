package inam.singletons;

import com.google.common.io.Resources;
import inam.utils.Utils;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class SecondConsumerSingleton {
	private static KafkaConsumer<String, String> instance;

	private SecondConsumerSingleton() {}

	public static KafkaConsumer<String, String> getInstance() {
		if (instance==null) {

			try (InputStream props = Resources.getResource(Utils.consumerProps).openStream()) {
				Properties properties = new Properties();
				properties.load(props);
				instance = new KafkaConsumer<>(properties);
				instance.subscribe(Arrays.asList(Utils.TOPIC_TWO));
			}
			catch (IOException ex) {
				System.err.println("Error occurred while creating SecondConsumerSingleton...");
			}
		}
		return instance;
	}
}
