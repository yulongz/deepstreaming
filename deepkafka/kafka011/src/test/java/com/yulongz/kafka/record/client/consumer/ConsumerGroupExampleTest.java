package com.yulongz.kafka.record.client.consumer;

import org.junit.Test;
import utils.PropsUtil;

/**
 * Created by kafka on 17-12-26.
 */
public class ConsumerGroupExampleTest {
    @Test
    public void testConsumerGroupExample(){
        String bootstrap = PropsUtil.loadProps("my.properties").getProperty("bootstrap.servers");
        String groupId = PropsUtil.loadProps("my.properties").getProperty("kafka.group.id");
        String topic = PropsUtil.loadProps("my.properties").getProperty("consumeravrotopic");
        String url = PropsUtil.loadProps("my.properties").getProperty("kafka.schema.registry.url");

        ConsumerGroupExample consumerGroupExample = new ConsumerGroupExample
                (bootstrap, groupId, topic, url);

        consumerGroupExample.testConsumer();
    }
}
