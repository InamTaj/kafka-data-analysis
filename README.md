# kafka-data-analysis
A messaging application that uses Apache Kafka to ingest, process, republish and reprocess data.

#### Project Structure
 * [src](./src)
   * [main](./src/main)
       - [java](./src/main/java)
         + [inam](./src/main/java/inam)
             * [Consumer.java](./src/main/java/inam/Consumer.java)
             * [Producer.java](./src/main/java/inam/Producer.java)
             * [Run.java](./src/main/java/inam/Run.java)
         + [models](./src/main/java/inam/models)
           * [SensorInput.java](./src/main/java/inam/models/SensorInput.java)
           * [SensorOutput.java](./src/main/java/inam/models/SensorOutput.java)
         + [utils](./src/main/java/inam/utils)
             * [JsonDeserializer.java](./src/main/java/inam/utils/JsonDeserializer.java)
             * [JsonSerializer.java](./src/main/java/inam/utils/JsonSerializer.java)
             * [ModelUtils.java](./src/main/java/inam/utils/ModelUtils.java)
       - [resources](./src/main/resources)
           * [consumer.props](./src/main/resources/consumer.props)
           * [producer.props](./src/main/resources/producer.props)
 * [MOCK_DATA.json](./MOCK_DATA.json)
 * [pom.xml](./pom.xml)
 * [README.md](./README.md)


#### Explanation
+ `Run.java` = main executable file that takes an argument which specifies what producer/consumer to run.
+ `Producer.java` = reads data from mock file, converts it into `SensorInput` Model and writes the models into kafka topic #1: `inamTopic`
+ `Consumer.java` = reads data from topic #1: `inamTopic` and processes it into `SensorInput` Model
+ `resources/consumer.props` = Properties files for a Kafka Consumer
+ `resources/producer.props` = Properties files for a Kafka Producer
+ `models/SensorInput.java` = A POJO representing incoming JSON data.
+ `models/SensorOutput.java` = A POJO representing formatted JSON data ready for output.
+ `models/SensorDataClass.java` = A POJO representing final computed sensor data for output.
+ `singletons/SecondProducer.java` = A singleton that returns a new producer sending transformed JSON objects to Topic #2.
+ `singletons/SecondConsumer.java` = A singleton that returns a new consumer listening to Topic #2, getting it's data and processing sensor data from it.
+ `singletons/FileWriterSingleton.java` = A singleton that returns a BufferedWriter stream object to write on file.
+ `utils/ModelUtils.java` = A utility class that contains common functions that are used in Consumer or Producer classes.
+ `utils/Utils.java` = A utility class that contains common properties to be used across the application.
+ `pom.xml` = Maven project's pom file representing project's lifecycle. See How-to-run section for more details.
+ `MOCK_DATA.json` = This JSON file contains mock data from multiple sensor devices.
+ `TRANSFORMED_DATA_FILE.json` = This JSON file on classpath will contain formattted JSON data.
+ `FINAL_COST_PER_HOUR.json` = This JSON file on classpath will contain final running cost for each sensor, i.e., objects of `SensorDataClass`.


#### How-to-run
```bash
$ mvn clean package
$ target/kafka-data-analysis producer
                                OR
$ target/kafka-data-analysis consumer --transform-data
                                OR
$ target/kafka-data-analysis consumer --sensor-cost
```
