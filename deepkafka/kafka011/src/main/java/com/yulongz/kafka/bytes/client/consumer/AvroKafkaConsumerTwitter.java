package com.yulongz.kafka.bytes.client.consumer;

/**
 * Project Name:avrolearn
 * Package Name:avroexample.example.kafka
 * Date:17-10-9 上午11:03
 * AUTHOR by zhangyulong
 * Email:sky.zyl@hotmail.com
 */

import com.yulongz.kafka.bytes.client.producer.AvroKafkaProducter;
import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AvroKafkaConsumerTwitter {
    static Logger logger = LoggerFactory.getLogger("AvroKafkaConsumerTwitter");
    private final ConsumerConnector consumer;
    private final String topic;

    public AvroKafkaConsumerTwitter(String zookeeper, String groupId, String topic) {
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeper);
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", "500");
        props.put("zookeeper.sync.time.ms", "250");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "smallest");

        consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
        this.topic = topic;
    }

    public void testConsumer() {
        Map<String, Integer> topicCount = new HashMap<String, Integer>();
        topicCount.put(topic, 1);

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumer.createMessageStreams(topicCount);
        List<KafkaStream<byte[], byte[]>> streams = consumerStreams.get(topic);
        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(AvroKafkaProducter.USER_SCHEMA);
        for (final KafkaStream stream : streams) {
            ConsumerIterator it = stream.iterator();
            while (it.hasNext()) {
                MessageAndMetadata messageAndMetadata = it.next();
                String key = new String((byte[]) messageAndMetadata.key());
                byte[] message = (byte[]) messageAndMetadata.message();
                Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(schema);
                GenericRecord record = recordInjection.invert(message).get();

                logger.info("key=" + key + ", time= " + record.get("time")
                        + ", site= " + record.get("site")
                        + ", ip=" + record.get("ip"));
            }
        }
        consumer.shutdown();
    }
}