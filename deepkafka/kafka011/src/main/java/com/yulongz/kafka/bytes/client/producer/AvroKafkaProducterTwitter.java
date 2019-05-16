package com.yulongz.kafka.bytes.client.producer;

/**
 * Project Name:avrolearn
 * Package Name:avroexample.example.kafka
 * Date:17-10-9 上午10:09
 * AUTHOR by zhangyulong
 * Email:sky.zyl@hotmail.com
 */
import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.PropsUtil;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class AvroKafkaProducterTwitter {
    static Logger logger = LoggerFactory.getLogger("AvroKafkaProducterTwitter");
    public static final String USER_SCHEMA = "{\"namespace\": \"com.yulongz.example.avro\", " +
            "\"type\": \"record\", " +
            "\"name\": \"page_visit\"," +
            "\"fields\": [" +
            "{\"name\": \"time\", \"type\": \"long\"}," +
            "{\"name\": \"site\", \"type\": \"string\"}," +
            "{\"name\": \"ip\", \"type\": \"string\"}" +
            "]}";

    public static void main(String[] args) throws InterruptedException, IOException {
        Properties props = new Properties();
        props.put("bootstrap.servers", PropsUtil.loadProps("my.properties").getProperty("bootstrap.servers"));
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(USER_SCHEMA);
        Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(schema);


        KafkaProducer<String, byte[]> producer = new KafkaProducer<String, byte[]>(props);

        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            long runtime = System.currentTimeMillis();
            String site = "www.avroexample.example.com";
            String ip = "192.168.2." + rnd.nextInt(255);

            GenericRecord avroRecord = new GenericData.Record(schema);
            avroRecord.put("time", runtime);
            avroRecord.put("site", site);
            avroRecord.put("ip", ip);

            byte[] bytes = recordInjection.apply(avroRecord);

            String topic = PropsUtil.loadProps("my.properties").getProperty("producerbytetopic");

            ProducerRecord<String, byte[]> record = new ProducerRecord<String, byte[]>(topic, "" + i, bytes);
            producer.send(record);
            Thread.sleep(250);
            logger.info(String.valueOf(i));
        }

        producer.close();
    }
}
