
package kafkastreams.dsl.window.sliding;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.ValueJoiner;

public class Kslidingjoin {

	public static void main(String[] args) throws Exception {
		String applicationId = "streams-dsl-window";
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
		KStream<String, Long> count1 = source.groupByKey().count("statestore1").toStream();
		KStream<String, Long> count2 = source.groupByKey().count("statestore2").toStream();
		// source.print();

		//这里一定要设置k,v1,v2的Serdes类型，默认都是String
//		 KStream<String, String> join = count1.join(count2, (s1value, s2value)
//		 -> Long.toString(s1value + s2value),
//		 JoinWindows.of(TimeUnit.SECONDS.toMillis(6)),
//		 Serdes.String(),Serdes.Long(),Serdes.Long());
		KStream<String, String> join = count1.join(count2, new MyValueJoiner(),
				JoinWindows.of(TimeUnit.SECONDS.toMillis(6)).before(0).after(0),
				Serdes.String(),Serdes.Long(),Serdes.Long());

		join.print();

		KafkaStreams streams = new KafkaStreams(builder, props);
		streams.start();

		Thread.sleep(20000L);

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

class MyValueJoiner implements ValueJoiner<Long, Long, String> {
	@Override
	public String apply(Long value1, Long value2) {
		return Long.toString(value1 + value2);
	}
}