package com.yulongz.kafka.record.spark;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import utils.PropsUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AvroSparkStreaming {
    public static void main(String[] args){
        SparkConf sparkConf = new SparkConf().setAppName("spark").setMaster("local[1]");
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(1000));

        String sourceTopic = PropsUtil.loadProps("my.properties").getProperty("sparksourcetopic");
        String groupId = PropsUtil.loadProps("my.properties").getProperty("spark.group.id");
        String bootstrapServers = PropsUtil.loadProps("my.properties").getProperty("bootstrap.servers");
        String url = PropsUtil.loadProps("my.properties").getProperty("kafka.schema.registry.url");

        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("group.id", groupId);
        kafkaParams.put("bootstrap.servers", bootstrapServers);
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", KafkaAvroDeserializer.class);
        kafkaParams.put("auto.offset.reset", "earliest");
        kafkaParams.put("schema.registry.url", url);


        HashSet<String> topics = new HashSet<>();
        topics.add(sourceTopic);

        JavaInputDStream<ConsumerRecord<Object, Object>> directStream =
                KafkaUtils.createDirectStream(jssc, LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.Subscribe(topics, kafkaParams));
        directStream.map((Function<ConsumerRecord<Object, Object>, Object>) v1 -> {
            GenericRecord value = (GenericRecord) v1.value();
            return value.toString();
        }).print(1);

        jssc.start();
        try {
            Thread.sleep(10000);
            jssc.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
