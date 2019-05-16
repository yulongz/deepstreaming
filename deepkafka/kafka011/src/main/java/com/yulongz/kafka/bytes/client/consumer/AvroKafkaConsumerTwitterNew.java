package com.yulongz.kafka.bytes.client.consumer;

import com.yulongz.kafka.bytes.client.producer.AvroKafkaProducter;
import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Properties;

/**
 * @author Tiny
 * @date 2017/12/18
 */
public class AvroKafkaConsumerTwitterNew {
    private static Logger logger = LoggerFactory.getLogger("AvroKafkaConsumerTwitterNew");
    private final KafkaConsumer consumer;
    private final String topic;

    public AvroKafkaConsumerTwitterNew(String bootstrap, String groupId, String topic){
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrap);
        props.put("group.id", groupId);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put("auto.offset.reset", "earliest");
        consumer = new KafkaConsumer<String, byte[]>(props);
        this.topic = topic;
    }

    public void testConsumer() {
        consumer.subscribe(Collections.singletonList(topic));
        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(AvroKafkaProducter.USER_SCHEMA);
        Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(schema);

        try {
            while (true) {
                ConsumerRecords<String, byte[]> records = consumer.poll(1000);
                for (ConsumerRecord<String, byte[]> record : records) {
                    GenericRecord genericRecord = recordInjection.invert(record.value()).get();
                    logger.info("key=" + record.key() + ", time= " + genericRecord.get("time")
                            + ", site= " + genericRecord.get("site")
                            + ", ip=" + genericRecord.get("ip"));
                }
            }
        } finally {
            consumer.close();
        }
    }
}
