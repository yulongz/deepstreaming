package com.yulongz.kafka.bytes.client.consumer;

import org.junit.Test;
import utils.PropsUtil;

/**
 * @author Tiny
 * @date 2017/12/18
 */
public class AvroKafkaConsumerNewTest {
    @Test
    public void testAvroKafkaConsumerNew(){
        String bootstrap = PropsUtil.loadProps("my.properties").getProperty("bootstrap.servers");
        String groupId = PropsUtil.loadProps("my.properties").getProperty("kafka.group.id");
        String topic = PropsUtil.loadProps("my.properties").getProperty("consumerbytetopic");

        AvroKafkaConsumerNew consumerNew = new AvroKafkaConsumerNew(bootstrap,groupId,topic);
        consumerNew.testConsumer();
    }
}
