package com.yulongz.kafka.bytes.client.consumer;

import org.junit.Test;
import utils.PropsUtil;

/**
 * @author Tiny
 * @date 2017/12/18
 */
public class AvroKafkaConsumerTwitterNewTest {
    @Test
    public void testAvroKafkaConsumerTwitterNew(){
        String bootstrap = PropsUtil.loadProps("my.properties").getProperty("bootstrap.servers");
        String groupId = PropsUtil.loadProps("my.properties").getProperty("kafka.group.id");
        String topic = PropsUtil.loadProps("my.properties").getProperty("consumerbytetopic");

        AvroKafkaConsumerTwitterNew kafkaConsumerTwitterNew =
                new AvroKafkaConsumerTwitterNew(bootstrap,
                        groupId,
                        topic);
        kafkaConsumerTwitterNew.testConsumer();
    }
}
