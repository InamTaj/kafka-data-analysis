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
           * [SensorDataClass.java](./src/main/java/inam/models/SensorDataClass.java)
         + [singletons](./src/main/java/inam/singletons)
           * [FileWriterSingleton.java](./src/main/java/inam/utils/FileWriterSingleton.java)
           * [SecondConsumerSingleton.java](./src/main/java/inam/utils/SecondConsumerSingleton.java)
           * [SecondProducerSingleton.java](./src/main/java/inam/utils/SecondProducerSingleton.java)
         + [utils](./src/main/java/inam/utils)
             * [JsonDeserializer.java](./src/main/java/inam/utils/JsonDeserializer.java)
             * [JsonSerializer.java](./src/main/java/inam/utils/JsonSerializer.java)
             * [ModelUtils.java](./src/main/java/inam/utils/ModelUtils.java)
             * [TimeUtils.java](./src/main/java/inam/utils/TimeUtils.java)
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
+ `utils/TimeUtils.java` = A utility class with essentail date-time conversion and calculation facilities and used by various classes.
+ `utils/Utils.java` = A utility class that contains common properties to be used across the application.
+ `pom.xml` = Maven project's pom file representing project's lifecycle. See How-to-run section for more details.
+ `MOCK_DATA.json` = This JSON file contains mock data from multiple sensor devices.
+ `TRANSFORMED_DATA_FILE.json` = This JSON file on classpath will contain formattted JSON data.
+ `FINAL_COST_PER_HOUR.json` = This JSON file on classpath will contain final running cost for each sensor, i.e., objects of `SensorDataClass`.


#### How-to-run
Prerequisites:
* Apache kafka
* Maven 3.0+
* JDK/JRE 1.8+
* zookeper running
* kafka server running

```bash
$ cd kafka-data-analysis
$ mvn clean package
$ target/kafka-data-analysis producer
                                OR
$ target/kafka-data-analysis consumer --transform-data
                                OR
$ target/kafka-data-analysis consumer --sensor-cost
```

#### Notes
- Each Producer and Consumer should be run in a separate Terminal. You can also use terminal multiplexers such as _byobou_.
- The formula to calculate cost might be incorrect because the statement `(Sum (Total power for one hour)` seemed imbigous. Nontheless it is a minor change in the respective function.
    + see function `getRunningCostPerHour()`
- Streams were not used because the operations to be performed on data took a lot of time, and I couldn't find reasonable time to research on Kafka Streams' API documentation.
