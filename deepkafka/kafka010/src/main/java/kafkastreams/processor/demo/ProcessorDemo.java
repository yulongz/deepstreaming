/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kafkastreams.processor.demo;

import org.apache.commons.io.FileUtils;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.processor.TopologyBuilder;
import org.apache.kafka.streams.state.Stores;
import java.io.File;
import java.util.Properties;

public class ProcessorDemo {

	public static void main(String[] args) throws Exception {

		String applicationId = "streams-demo-processor";
		String inputTopic = "streams-demo-input";
		String outputTopic = "streams-demo-processor-output";
		String bootstrapServers = "breath:9092";
		String zookeeper = "breath:2181/kafka01021";
		
		long random = System.currentTimeMillis();

		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
		props.put(StreamsConfig.CLIENT_ID_CONFIG, ""+random);//single machine run mutiple instances，need set unique clientId,used to check offset of every instance
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams/"+random);//single machine run mutiple instances，need set unique state.dir
		props.put(StreamsConfig.TIMESTAMP_EXTRACTOR_CLASS_CONFIG,
				"org.apache.kafka.streams.processor.WallclockTimestampExtractor");
		TopologyBuilder builder = new TopologyBuilder();
		builder.addSource("Source", inputTopic);
		builder.addProcessor("Process", new MyProcessorSupplier(), "Source");
		builder.addStateStore(Stores.create("Counts").withStringKeys().withIntegerValues().inMemory().build(),
				"Process");
		builder.addSink("Sink", outputTopic, "Process");
		KafkaStreams streams = new KafkaStreams(builder, props);
		streams.start();
		Thread.sleep(100000L);
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
