package inam.singletons;

import com.google.common.io.Resources;
import inam.utils.Utils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SecondProducerSingleton {
	private static Producer<String, String> instance = null;

	private SecondProducerSingleton() {}

	public static Producer getInstance() {
		if (instance ==null) {
			try (InputStream props = Resources.getResource(Utils.propsFileForProducer).openStream()) {
				Properties properties = new Properties();
				properties.load(props);
				instance = new KafkaProducer<>(properties);
			}
			catch (IOException ex) {
				System.err.println("Exception occurred while reading props for creating instance");
			}
		}
		return instance;
	}
}
