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
package kafkastreams.dsl.examples.wordcount;

import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.ValueMapper;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;

/**
 * Demonstrates, using the high-level KStream DSL, how to implement the
 * WordCount program that computes a simple word occurrence histogram from an
 * input text.
 *
 * In this example, the input stream reads from a topic named
 * "streams-file-input", where the values of messages represent lines of text;
 * and the histogram output is written to topic "streams-wordcount-output" where
 * each record is an updated count of a single word.
 *
 * Before running this example you must create the input topic and the output
 * topic (e.g. via bin/kafka-topics.sh --create ...), and write some data to the
 * input topic (e.g. via bin/kafka-console-producer.sh). Otherwise you won't see
 * any data arriving in the output topic.
 */
public class WordCountDemo {

	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "breath:9092");
		props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams");// state.dir

		// setting offset reset to earliest so that we can re-run the demo code
		// with the same pre-loaded data
		// Note: To re-run the demo, you need to use the offset reset tool:
		// https://cwiki.apache.org/confluence/display/KAFKA/Kafka+Streams+Application+Reset+Tool
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		KStreamBuilder builder = new KStreamBuilder();

		KStream<String, String> source = builder.stream("streams-file-input");
		source.print();

		KTable<String, Long> counts = source.flatMapValues(new ValueMapper<String, Iterable<String>>() {
			@Override
			public Iterable<String> apply(String value) {
				return Arrays.asList(value.toLowerCase(Locale.getDefault()).split(" "));
			}
		}).map(new KeyValueMapper<String, String, KeyValue<String, String>>() {
			@Override
			public KeyValue<String, String> apply(String key, String value) {
				return new KeyValue<>(value, value);
			}
		}).groupByKey().count("Counts");
		

		// need to override value serde to Long type
		counts.to(Serdes.String(), Serdes.Long(), "streams-wordcount-output");
		counts.print();

		KafkaStreams streams = new KafkaStreams(builder, props);
		// streams.cleanUp();
		streams.start();

		// usually the stream application would be running forever,
		// in this example we just let it run for some time and stop since the
		// input data is finite.
		Thread.sleep(5000L);

		streams.close();

		// 清空state store
		String appStateDir = props.get(StreamsConfig.STATE_DIR_CONFIG) + System.getProperty("file.separator")
				+ props.get(StreamsConfig.APPLICATION_ID_CONFIG);
		// System.out.println(appStateDir);
		FileUtils.deleteDirectory(new File(appStateDir));

		// 清空application相关中间topic以及input topic的offset
		String kafkaHome = System.getenv("KAFKA_HOME");
		Runtime runtime = Runtime.getRuntime();
		runtime.exec(kafkaHome + "/bin/kafka-streams-application-reset.sh " + "--application-id "
				+ props.get(StreamsConfig.APPLICATION_ID_CONFIG) + " " + "--bootstrap-servers breath:9092 "
				+ "--zookeeper breath:2181/kafka01021 " + "--input-topics streams-file-input");
	}
}
