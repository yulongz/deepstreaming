package com.yulongz.kafka.record.client.consumer.deprecated;

import org.junit.Test;
import utils.PropsUtil;

/**
 * Created by kafka on 17-12-19.
 */
public class ConsumerGroupExampleTest {
    @Test
    public void testConsumer(){

        String zooKeeper = PropsUtil.loadProps("my.properties").getProperty("kafka.zookeeper.connect");
        String groupId = PropsUtil.loadProps("my.properties").getProperty("kafka.group.id");
        String topic = PropsUtil.loadProps("my.properties").getProperty("consumeravrotopic");
        int threads = 3;
        String url = PropsUtil.loadProps("my.properties").getProperty("kafka.schema.registry.url");

        ConsumerGroupExample example = new ConsumerGroupExample(zooKeeper, groupId, topic, url);
        example.run(threads);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException ie) {

        }
        example.shutdown();
    }
}
