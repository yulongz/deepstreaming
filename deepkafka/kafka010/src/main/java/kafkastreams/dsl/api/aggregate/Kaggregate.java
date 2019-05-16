
package kafkastreams.dsl.api.aggregate;

import java.io.File;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;

public class Kaggregate {

	public static void main(String[] args) throws Exception {
		String applicationId = "streams-dslapi";
		String inputTopic = "streams-dslapi-input";
		// String outputTopic = "streams-dslapi-output";
		String bootstrapServers = "breath:9092";
		String zookeeper = "breath:2181/kafka01021";

		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams");
		props.put(StreamsConfig.TIMESTAMP_EXTRACTOR_CLASS_CONFIG,
				"org.apache.kafka.streams.processor.WallclockTimestampExtractor");

		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		KStreamBuilder builder = new KStreamBuilder();

		KStream<String, String> source = builder.stream(inputTopic);
		// source.print();

		// jre8
//		KTable<String, Integer> aggregate = source.groupByKey().aggregate(
//				() -> 0, /* initializer */
//				(aggKey, newValue, aggValue) -> aggValue + newValue.length(), /* adder */
//				Serdes.Integer(), /* serde for aggregate value */
//				"statestore" /* state store name */);

		// jre7
		KTable<String, Integer> aggregate = source.groupByKey().aggregate(new Initializer<Integer>() {
			@Override
			public Integer apply() {
				return 0;
			}
		}, new Aggregator<String, String, Integer>() {
			@Override
			public Integer apply(String key, String value, Integer aggregate) {
				return aggregate + value.length();
			}
		}, Serdes.Integer(), "statestore");

		aggregate.print();

		KafkaStreams streams = new KafkaStreams(builder, props);
		streams.start();

		Thread.sleep(5000L);

		streams.close();

		// 清空state store
		String appStateDir = props.get(StreamsConfig.STATE_DIR_CONFIG) + System.getProperty("file.separator")
				+ props.get(StreamsConfig.APPLICATION_ID_CONFIG);
		FileUtils.deleteDirectory(new File(appStateDir));

		// 清空application相关中间topic以及input topic的offset
		String kafkaHome = System.getenv("KAFKA_HOME");
		Runtime runtime = Runtime.getRuntime();
		runtime.exec(kafkaHome + "/bin/kafka-streams-application-reset.sh " + "--application-id " + applicationId + " "
				+ "--bootstrap-servers " + bootstrapServers + " --zookeeper " + zookeeper + " --input-topics "
				+ inputTopic);
	}
}
