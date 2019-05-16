package example.kafka;

/**
 * Project Name:avrolearn
 * Package Name:example.kafka
 * Date:17-10-9 下午2:26
 * AUTHOR by zhangyulong
 * Email:sky.zyl@hotmail.com
 */

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

//kafka version >= 0.9
public class AvroKafkaConsumerTwitterNew {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger("AvroKafkaConsumer");
        Properties props = new Properties();
        props.put("bootstrap.servers", "breath:9092");
        props.put("group.id", "testgrouptwitternew");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put("auto.offset.reset", "earliest");
        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<String, byte[]>(props);
        String topic = "avrotest";

        consumer.subscribe(Collections.singletonList(topic));
        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(AvroKafkaProducter.USER_SCHEMA);
        Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(schema);

        try {
            while (true) {
                ConsumerRecords<String, byte[]> records = consumer.poll(1000);
                for (ConsumerRecord<String, byte[]> record : records) {
                    GenericRecord genericRecord = recordInjection.invert(record.value()).get();
                    logger.info("key=" + record.key() + ", str1= " + genericRecord.get("str1")
                            + ", str2= " + genericRecord.get("str2")
                            + ", int1=" + genericRecord.get("int1"));
                }
            }
        } finally {
            consumer.close();
        }
    }
}