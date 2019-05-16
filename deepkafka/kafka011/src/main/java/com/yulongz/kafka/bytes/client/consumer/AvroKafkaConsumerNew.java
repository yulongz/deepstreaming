package com.yulongz.kafka.bytes.client.consumer;

/**
 * Project Name:avrolearn
 * Package Name:avroexample.example.kafka
 * Date:17-10-9 下午2:26
 * AUTHOR by zhangyulong
 * Email:sky.zyl@hotmail.com
 */

import com.yulongz.kafka.bytes.client.producer.AvroKafkaProducter;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

//kafka version >= 0.9
public class AvroKafkaConsumerNew {
    private static Logger logger = LoggerFactory.getLogger("AvroKafkaConsumerNew");
    private final KafkaConsumer consumer;
    private final String topic;

    public AvroKafkaConsumerNew(String bootstrap, String groupId, String topic){
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
        GenericDatumReader genericDatumReader = new GenericDatumReader(schema);
        try {
            while (true) {
                ConsumerRecords<String, byte[]> records = consumer.poll(1000);
                for (ConsumerRecord<String, byte[]> record : records) {
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(record.value());
                    BinaryDecoder binaryDecoder = DecoderFactory.get().binaryDecoder(byteArrayInputStream, null);
                    GenericData.Record avroRecord = new GenericData.Record(schema);
                    GenericData.Record genericRecord = (GenericData.Record) genericDatumReader.read(avroRecord, binaryDecoder);
                    logger.info("key=" + record.key() + ", time= " + genericRecord.get("time")
                            + ", site= " + genericRecord.get("site")
                            + ", ip=" + genericRecord.get("ip"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}