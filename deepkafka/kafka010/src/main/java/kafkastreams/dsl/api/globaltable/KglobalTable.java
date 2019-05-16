
package kafkastreams.dsl.api.globaltable;

import java.io.File;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

public class KglobalTable {

	@SuppressWarnings("unused")
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

		String storeName = applicationId + "-globaltable";
		GlobalKTable<String, String> source = builder.globalTable(Serdes.String(), Serdes.String(), inputTopic, storeName);
		
		KafkaStreams streams = new KafkaStreams(builder, props);
		streams.start();

		ReadOnlyKeyValueStore<String, String> view = streams.store(storeName, QueryableStoreTypes.keyValueStore());

		//System.out.println(view.get("1"));	
		 KeyValueIterator<String,String> iter = view.all();
		 while(iter.hasNext()){
	     KeyValue<String,String> str = iter.next();
		  System.out.println("key:"+str.key+";value:"+str.value);
		 }
		
		
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
