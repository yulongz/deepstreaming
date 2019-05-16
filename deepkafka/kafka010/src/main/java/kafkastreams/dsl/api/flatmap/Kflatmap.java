
package kafkastreams.dsl.api.flatmap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KeyValueMapper;

public class Kflatmap {

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

		// Jre8
		// KStream<String, String> flatMap = source.flatMap((key, value) -> {
		// List<KeyValue<String, String>> result = new LinkedList<>();
		// result.add(KeyValue.pair(value.toUpperCase(), "Upper"));
		// result.add(KeyValue.pair(value.toLowerCase(), "Lower"));
		// return result;
		// });

		// Jre7
		KStream<String, String> flatMap = source
				.flatMap(new KeyValueMapper<String, String, Iterable<KeyValue<String, String>>>() {
					@Override
					public Iterable<KeyValue<String, String>> apply(String key, String value) {
						List<KeyValue<String, String>> result = new ArrayList<>(2);
						result.add(KeyValue.pair(value.toUpperCase(), "Upper"));
						result.add(KeyValue.pair(value.toLowerCase(), "Lower"));
						return result;
					}
				});
		flatMap.print();

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

// implements class
class MyKeyValueMapper implements KeyValueMapper<String, String, Iterable<KeyValue<String, String>>> {
	@Override
	public Iterable<KeyValue<String, String>> apply(String key, String value) {
		List<KeyValue<String, String>> result = new ArrayList<>(2);
		result.add(KeyValue.pair(value.toUpperCase(), "Upper"));
		result.add(KeyValue.pair(value.toLowerCase(), "Lower"));
		return result;
	}
}
