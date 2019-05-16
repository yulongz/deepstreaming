package com.yulongz.kafka.record.storm.trident;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.kafka.spout.KafkaSpoutRetryExponentialBackoff;
import org.apache.storm.kafka.spout.KafkaSpoutRetryService;
import org.apache.storm.kafka.spout.trident.KafkaTridentSpoutOpaque;
import org.apache.storm.kafka.trident.TridentKafkaStateFactory;
import org.apache.storm.kafka.trident.TridentKafkaStateUpdater;
import org.apache.storm.kafka.trident.mapper.FieldNameBasedTupleToKafkaMapper;
import org.apache.storm.kafka.trident.selector.DefaultTopicSelector;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.tuple.Fields;
import utils.PropsUtil;

import java.util.Properties;

import static org.apache.storm.kafka.spout.KafkaSpoutConfig.FirstPollOffsetStrategy.EARLIEST;

/**
 * Created by kafka on 17-12-23.
 */
public class AvroKafkaTridentExample {
    public static String bootstrapServers = PropsUtil.loadProps("my.properties").getProperty("bootstrap.servers");
    public static String spoutTopic = PropsUtil.loadProps("my.properties").getProperty("producerbytetopic");
    public static String groupId = PropsUtil.loadProps("my.properties").getProperty("storm.group.id");
    public static String url = PropsUtil.loadProps("my.properties").getProperty("kafka.schema.registry.url");
    public static String boltTopic = PropsUtil.loadProps("my.properties").getProperty("kafkabolttopic");

    public static void main(String[] args) {
        TridentTopology topology = new TridentTopology();

        Properties consumerprops = new Properties();
        consumerprops.put("schema.registry.url", url);

        KafkaSpoutConfig<String, Object> spoutConfig = KafkaSpoutConfig.builder
                (bootstrapServers, spoutTopic)
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

        Stream stream = topology.newStream("kafkaspout", new KafkaTridentSpoutOpaque(spoutConfig))
                                .parallelismHint(1);
//                                .each(new Fields("key", "value"), new AvroFunctionTest(), new Fields());

        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", bootstrapServers);
        producerProps.put("acks", "1");
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        producerProps.put("schema.registry.url", url);

        TridentKafkaStateFactory stateFactory = new TridentKafkaStateFactory()
                .withProducerProperties(producerProps)
                .withKafkaTopicSelector(new DefaultTopicSelector(boltTopic))
                .withTridentTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper("key", "value"));
        stream.partitionPersist(stateFactory, new Fields("key", "value"), new TridentKafkaStateUpdater(), new Fields( ));

        Config config = new Config();
        config.setDebug(false);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("avro-kafka-trident", config, topology.build());

    }

    protected static KafkaSpoutRetryService getRetryService() {
        return new KafkaSpoutRetryExponentialBackoff(KafkaSpoutRetryExponentialBackoff.TimeInterval.microSeconds(500),
                KafkaSpoutRetryExponentialBackoff.TimeInterval.milliSeconds(2), Integer.MAX_VALUE, KafkaSpoutRetryExponentialBackoff.TimeInterval.seconds(10));
    }
}
