package com.yulongz.kafka.record.storm.normal;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.bolt.KafkaBolt;
import org.apache.storm.kafka.bolt.mapper.FieldNameBasedTupleToKafkaMapper;
import org.apache.storm.kafka.bolt.selector.DefaultTopicSelector;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.kafka.spout.KafkaSpoutRetryExponentialBackoff;
import org.apache.storm.kafka.spout.KafkaSpoutRetryExponentialBackoff.*;
import org.apache.storm.kafka.spout.KafkaSpoutRetryService;
import org.apache.storm.topology.TopologyBuilder;
import utils.PropsUtil;

import java.util.Properties;

import static org.apache.storm.kafka.spout.KafkaSpoutConfig.FirstPollOffsetStrategy.EARLIEST;


/**
 * Created by kafka on 17-12-20.
 */
public class AvroKafkaExample {
    public static String bootstrapServers = PropsUtil.loadProps("my.properties").getProperty("bootstrap.servers");
    public static String spoutTopic = PropsUtil.loadProps("my.properties").getProperty("kafkaspouttopic");
    public static String groupId = PropsUtil.loadProps("my.properties").getProperty("storm.group.id");
    public static String url = PropsUtil.loadProps("my.properties").getProperty("kafka.schema.registry.url");
    public static String boltTopic = PropsUtil.loadProps("my.properties").getProperty("kafkabolttopic");

    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();

        Properties consumerprops = new Properties();
        consumerprops.put("schema.registry.url", url);

        KafkaSpoutConfig<String, Object> spoutConfig = KafkaSpoutConfig.builder(bootstrapServers, spoutTopic)
                            .setProp(consumerprops)
                            .setProp(ConsumerConfig.GROUP_ID_CONFIG, groupId)
                            .setProp(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
                            .setProp(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,KafkaAvroDeserializer.class)
                            .setKey(StringDeserializer.class)
                            .setValue(KafkaAvroDeserializer.class)
                            .setRetry(getRetryService())
                            .setOffsetCommitPeriodMs(10_000)
                            .setFirstPollOffsetStrategy(EARLIEST)
                            .setMaxUncommittedOffsets(250)
                            .build();
        KafkaSpout spout = new KafkaSpout(spoutConfig);

        builder.setSpout("kafka-spout", spout, 1);
//        builder.setBolt("kafka-bolt", new AvroTestBolt(), 1).shuffleGrouping("kafka-spout");

        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", bootstrapServers);
        producerProps.put("acks", "1");
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        producerProps.put("schema.registry.url", url);

        KafkaBolt bolt = new KafkaBolt()
                .withProducerProperties(producerProps)
                .withTopicSelector(new DefaultTopicSelector(boltTopic))
                .withTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper("key", "value"));
        builder.setBolt("kafka-bolt", bolt, 1).shuffleGrouping("kafka-spout");

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
