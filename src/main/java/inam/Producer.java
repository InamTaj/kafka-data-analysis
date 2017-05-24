package inam;

import com.google.common.io.Resources;
import inam.utils.ModelUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Producer {
	public static void main(String... args) throws IOException {
		org.apache.kafka.clients.producer.Producer<String, String> producer = null;
		ProducerRecord<String, String> record = null;
		String mockData = "MOCK_DATA.json";
		String propsFile = "producer.props";
		String topicName = "inamTopic";

		try (InputStream props = Resources.getResource(propsFile).openStream()) {
			Properties properties = new Properties();
			properties.load(props);
			producer = new KafkaProducer<>(properties);
		}

		try (BufferedReader br = new BufferedReader(new FileReader(mockData))) {
			String line;
			while ((line = br.readLine()) != null) {
				// process the line
				String modelAsString = ModelUtils.parseStringToSensorInputModel(line, ModelUtils.MessageType.OUTGOING).toString();
                record = new ProducerRecord<>(topicName, modelAsString);
                producer.send(record);
                // console output
				System.out.println(modelAsString);
			}
		} finally {
			producer.close();
		}
	}


}
