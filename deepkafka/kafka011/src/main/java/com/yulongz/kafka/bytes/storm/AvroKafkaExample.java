package com.yulongz.kafka.bytes.storm;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.kafka.spout.KafkaSpoutRetryExponentialBackoff;
import org.apache.storm.kafka.spout.KafkaSpoutRetryExponentialBackoff.TimeInterval;
import org.apache.storm.kafka.spout.KafkaSpoutRetryService;
import org.apache.storm.topology.TopologyBuilder;
import utils.PropsUtil;


import static org.apache.storm.kafka.spout.KafkaSpoutConfig.FirstPollOffsetStrategy.EARLIEST;

/**
 * Created by kafka on 17-12-20.
 */
public class AvroKafkaExample {
    public static String bootstrapServers = PropsUtil.loadProps("my.properties").getProperty("bootstrap.servers");
    public static String spoutTopic = PropsUtil.loadProps("my.properties").getProperty("producerbytetopic");
    public static String groupId = PropsUtil.loadProps("my.properties").getProperty("storm.group.id");

    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();

        KafkaSpoutConfig<String, byte[]> spoutConfig = KafkaSpoutConfig.builder(bootstrapServers, spoutTopic)
                .setProp(ConsumerConfig.GROUP_ID_CONFIG, groupId)
                .setProp(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
                .setProp(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class)
                .setValue(ByteArrayDeserializer.class)
                .setRetry(getRetryService())
                .setOffsetCommitPeriodMs(10_000)
                .setFirstPollOffsetStrategy(EARLIEST)
                .setMaxUncommittedOffsets(250)
                .build();

        KafkaSpout spout = new KafkaSpout(spoutConfig);

        builder.setSpout("kafka-spout", spout, 1);
        builder.setBolt("kafka-bolt", new KafkaSpoutTestBolt(), 1).shuffleGrouping("kafka-spout");

        Config config = new Config();
        config.setDebug(false);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("avro-kafka", config, builder.createTopology());

    }

    protected static KafkaSpoutRetryService getRetryService() {
        return new KafkaSpoutRetryExponentialBackoff(TimeInterval.microSeconds(500),
                TimeInterval.milliSeconds(2), Integer.MAX_VALUE, TimeInterval.seconds(10));
    }
}
