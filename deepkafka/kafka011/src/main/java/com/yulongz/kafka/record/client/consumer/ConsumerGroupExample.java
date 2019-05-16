package com.yulongz.kafka.record.client.consumer;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Properties;

/**
 * Created by kafka on 17-12-21.
 */
public class ConsumerGroupExample {
    private static Logger logger = LoggerFactory.getLogger("ConsumerGroupExample");
    private final KafkaConsumer consumer;
    private final String topic;

    public ConsumerGroupExample(String bootstrap, String groupId, String topic, String url){
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrap);
        props.put("group.id", groupId);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        props.put("auto.offset.reset", "earliest");
        props.put("schema.registry.url", url);
        consumer = new KafkaConsumer<String, GenericRecord>(props);
        this.topic = topic;
    }

    public void testConsumer() {
        consumer.subscribe(Collections.singletonList(topic));
        try {
            while (true) {
                ConsumerRecords<String, GenericRecord> records = consumer.poll(1000);
                for(ConsumerRecord<String,GenericRecord> record : records){
                    System.out.println(record.key()+"=========="+record.value());
                }
            }
        } finally {
            consumer.close();
        }
    }
}
