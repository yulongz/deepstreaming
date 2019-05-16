package com.yulongz.kafka.record.streams.processor;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroDeserializer;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerializer;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.TopologyBuilder;
import utils.PropsUtil;

import java.util.Collections;
import java.util.Properties;

/**
 * Created by hadoop on 18-1-1.
 */
public class ProcessorDemo {
        public static String bootstrapServers = PropsUtil.loadProps("my.properties").getProperty("bootstrap.servers");
        public static String inputTopic = PropsUtil.loadProps("my.properties").getProperty("streamssourcetopic");
        public static String applicationId = PropsUtil.loadProps("my.properties").getProperty("streams.application.id");
        public static String url = PropsUtil.loadProps("my.properties").getProperty("kafka.schema.registry.url");
        public static String outputTopic = PropsUtil.loadProps("my.properties").getProperty("streamstargettopic");


        public static void main(String[] args) throws Exception {

            long random = System.currentTimeMillis();

            Properties props = new Properties();
            props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
            props.put(StreamsConfig.CLIENT_ID_CONFIG, ""+random);//single machine run mutiple instances，need set unique clientId,used to check offset of every instance
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
            props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams/"+random);//single machine run mutiple instances，need set unique state.dir
            props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG,
                    "org.apache.kafka.streams.processor.WallclockTimestampExtractor");
            props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, url);

            boolean isKey = false;
            GenericAvroDeserializer genericAvroDeserializer = new GenericAvroDeserializer();
            genericAvroDeserializer.configure(Collections.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, url),isKey);


            GenericAvroSerializer genericAvroSerializer = new GenericAvroSerializer();
            genericAvroSerializer.configure(Collections.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, url),isKey);

            TopologyBuilder builder = new TopologyBuilder();
            builder.addSource("Source", new StringDeserializer(),genericAvroDeserializer,inputTopic);
            builder.addProcessor("Process", new MyProcessorSupplier(), "Source");
            builder.addSink("Sink", outputTopic,new StringSerializer(),genericAvroSerializer, "Process");
            KafkaStreams streams = new KafkaStreams(builder, props);
            streams.start();
            Thread.sleep(50000L);
            streams.close();

    }

}
