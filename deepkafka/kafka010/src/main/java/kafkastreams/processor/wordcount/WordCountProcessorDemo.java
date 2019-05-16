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
package kafkastreams.processor.wordcount;

import org.apache.commons.io.FileUtils;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.ProcessorSupplier;
import org.apache.kafka.streams.processor.TopologyBuilder;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;
import java.io.File;
import java.util.Locale;
import java.util.Properties;

/**
 * Demonstrates, using the low-level Processor APIs, how to implement the
 * WordCount program that computes a simple word occurrence histogram from an
 * input text.
 *
 * In this example, the input stream reads from a topic named
 * "streams-file-input", where the values of messages represent lines of text;
 * and the histogram output is written to topic
 * "streams-wordcount-processor-output" where each record is an updated count of
 * a single word.
 *
 * Before running this example you must create the input topic and the output
 * topic (e.g. via bin/kafka-topics.sh --create ...), and write some data to the
 * input topic (e.g. via bin/kafka-console-producer.sh). Otherwise you won't see
 * any data arriving in the output topic.
 */
public class WordCountProcessorDemo {
	private static class MyProcessorSupplier implements ProcessorSupplier<String, String> {
		@Override
		public Processor<String, String> get() {
			return new Processor<String, String>() {
				private ProcessorContext context;
				private KeyValueStore<String, Integer> kvStore;

				@Override
				@SuppressWarnings("unchecked")
				public void init(ProcessorContext context) {
					this.context = context;
					this.context.schedule(5000);
					this.kvStore = (KeyValueStore<String, Integer>) context.getStateStore("Counts");
				}
				@Override
				public void process(String dummy, String line) {
					String[] words = line.toLowerCase(Locale.getDefault()).split(" ");
					System.out.println("line:" + line);
					for (String word : words) {
						Integer oldValue = this.kvStore.get(word);

						if (oldValue == null) {
							this.kvStore.put(word, 1);
						} else {
							this.kvStore.put(word, oldValue + 1);
						}
					}
					context.commit();
				}
				@Override
				public void punctuate(long timestamp) {
					try (KeyValueIterator<String, Integer> iter = this.kvStore.all()) {
						System.out.println("----------- " + timestamp + "----------- ");
						//System.out.println(TimestampUtil.TimestampFormat(timestamp));
						while (iter.hasNext()) {
							KeyValue<String, Integer> entry = iter.next();
							System.out.println("[" + entry.key + ", " + entry.value + "]");
							context.forward(entry.key, entry.value.toString());
						}
					}
				}
				@Override
				public void close() {
				}
			};
		}
	}
	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount-processor");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "breath:9092");
		props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams");
		props.put(StreamsConfig.TIMESTAMP_EXTRACTOR_CLASS_CONFIG,"org.apache.kafka.streams.processor.WallclockTimestampExtractor");
		TopologyBuilder builder = new TopologyBuilder();
		builder.addSource("Source", "streams-file-input");
		builder.addProcessor("Process", new MyProcessorSupplier(), "Source");
		builder.addStateStore(Stores.create("Counts").withStringKeys().withIntegerValues().inMemory().build(),
				"Process");
		builder.addSink("Sink", "streams-wordcount-processor-output", "Process");
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
		runtime.exec(kafkaHome + "/bin/kafka-streams-application-reset.sh " + "--application-id "
				+ props.get(StreamsConfig.APPLICATION_ID_CONFIG) + " " + "--bootstrap-servers breath:9092 "
				+ "--zookeeper breath:2181/kafka01021 " + "--input-topics streams-file-input");
	}
}
