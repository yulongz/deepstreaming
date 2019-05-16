package com.yulongz.kafka.bytes.client.consumer;

import org.junit.Test;
import utils.PropsUtil;

import java.io.IOException;

/**
 * @author Tiny
 * @date 2017/12/18
 */
public class AvroKafkaConsumerTest {
    @Test
    public void testAvroKafkaConsumer() throws IOException {
        String zooKeeper = PropsUtil.loadProps("my.properties").getProperty("kafka.zookeeper.connect");
        String groupId = PropsUtil.loadProps("my.properties").getProperty("kafka.group.id");
        String topic = PropsUtil.loadProps("my.properties").getProperty("consumerbytetopic");
        AvroKafkaConsumer simpleConsumer =
                new AvroKafkaConsumer(zooKeeper, groupId, topic);
        simpleConsumer.testConsumer();
    }
}
