
package kafkastreams.dsl.window.session;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.apache.kafka.streams.kstream.Windowed;

public class Ksessioncount {

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
		// source.print();
		System.out.println(System.currentTimeMillis());
		// sessionwindow

		KTable<Windowed<String>, Long> count = source.groupByKey(Serdes.String(), Serdes.String())//这里的groupBy必须增加Serdes，否则会报空指针异常
				.count(SessionWindows.with(TimeUnit.SECONDS.toMillis(6)), "statestore");
		// 关于key里面最新的两条数据小于设置值，则为sessionwindow会一直维持，如果大于则生成新的seesionwindow。其实就是session
		// timeout的概念

		count.print();

		KafkaStreams streams = new KafkaStreams(builder, props);
		streams.start();

		Thread.sleep(30000L);

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
