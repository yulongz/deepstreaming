package com.yulongz.kafka.bytes.client.consumer;

import org.junit.Test;
import utils.PropsUtil;

/**
 * @author Tiny
 * @date 2017/12/18
 */
public class AvroKafkaConsumerTwitterTest {
    @Test
    public void testAvroKafkaConsumerTwitter(){
        String zooKeeper = PropsUtil.loadProps("my.properties").getProperty("kafka.zookeeper.connect");
        String groupId = PropsUtil.loadProps("my.properties").getProperty("kafka.group.id");
        String topic = PropsUtil.loadProps("my.properties").getProperty("consumerbytetopic");

        AvroKafkaConsumerTwitter simpleConsumer =
                new AvroKafkaConsumerTwitter(zooKeeper,
                        groupId,
                        topic);
        simpleConsumer.testConsumer();
    }
}
