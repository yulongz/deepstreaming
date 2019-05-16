package com.yulongz.ignite.streaming.kafka;

import kafka.consumer.ConsumerConfig;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.stream.kafka.KafkaStreamer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static junit.framework.TestCase.fail;

/**
 * @author hadoop
 * @date 18-1-8
 */
public class IgniteKafkaDemo {
    private static final String CACHE_NAME = IgniteKafkaDemo.class.getSimpleName();

    public static void main(String[] args) throws TimeoutException, InterruptedException {
        try(Ignite ignite = Ignition.start("default-config.xml")){
            try(IgniteCache<String, String> cache = ignite.getOrCreateCache(CACHE_NAME)){
                //producer kafka data
                produceStream("test");
                consumerStream(ignite,"test");
            } finally {
                ignite.destroyCache(CACHE_NAME);
            }
        }
    }

    private static void consumerStream(Ignite ignite, String topic)
            throws TimeoutException, InterruptedException {
        KafkaStreamer<String, String> kafkaStmr = null;

        try (IgniteDataStreamer<String, String> stmr = ignite.dataStreamer(CACHE_NAME)) {
            stmr.allowOverwrite(true);
            stmr.autoFlushFrequency(10);

            // Configure Kafka streamer.
            kafkaStmr = new KafkaStreamer<>();
            // Get the cache.
            IgniteCache<String, String> cache = ignite.cache(CACHE_NAME);
            // Set Ignite instance.
            kafkaStmr.setIgnite(ignite);
            // Set data streamer instance.
            kafkaStmr.setStreamer(stmr);
            // Set the topic.
            kafkaStmr.setTopic(topic);
            // Set the number of threads.
            kafkaStmr.setThreads(1);
            // Set the consumer configuration.
            kafkaStmr.setConsumerConfig(createDefaultConsumerConfig("localhost:2181/kafka10","ignite"));

            final CountDownLatch latch = new CountDownLatch(10);

            kafkaStmr.setMultipleTupleExtractor(
                    msg -> {
                        Map<String, String> entries = new HashMap<>();

                        try {
                            String key = new String(msg.key());
                            String val = new String(msg.message());

                            latch.countDown();

                            entries.put(key, val);
                        }
                        catch (Exception ex) {
                            fail("Unexpected error." + ex);
                        }

                        return entries;
                    });

            // Start kafka streamer.
            kafkaStmr.start();

            latch.await(10, TimeUnit.SECONDS);

            stmr.flush();

            System.out.println(" Cache count is " + cache.size());

            if (cache.size() > 0) {
                System.out.println(" Cache content is " + cache.iterator().next());
            }
        }
        finally {
            if (kafkaStmr != null) {
                kafkaStmr.stop();
            }
        }
    }

    private static ConsumerConfig createDefaultConsumerConfig(String zookeeper, String grpId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeper);
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", grpId);
        props.put("auto.offset.reset", "smallest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        return new ConsumerConfig(props);
    }

    private static void produceStream(String topic) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        // Generate random subnets.
        List<Integer> subnet = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            subnet.add(i);
        }
        Collections.shuffle(subnet);

        String KEY_PREFIX = "192.168.2.";
        String VALUE_URL = ",www.example.com,";

        for (int evt = 0; evt < 100; evt++) {
            long runtime = System.currentTimeMillis();

            String ip = KEY_PREFIX + subnet.get(evt);
            String msg = runtime + VALUE_URL + ip;

            producer.send(new ProducerRecord<>(topic, ip, msg));
        }

        producer.flush();
        producer.close();
    }
}
