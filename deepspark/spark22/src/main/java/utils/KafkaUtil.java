package utils;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @author Tiny
 * @date 2017/11/20
 */
public class KafkaUtil {

    public static void produceData(String key, String value){
        Properties props = getProps();
        produceData(getTopic(), key, value, props);
    }

    public static void produceData(String key, String value, Properties properties){
        Properties props = getProps();
        props.putAll(properties);
        produceData(getTopic(),key, value, props);
    }

    public static void produceData(String topic, String key, String value){
        Properties props = getProps();
        produceData(topic, key, value, props);
    }

    public static void produceData(String topic, String key, String value, Properties properties){
        Properties props = getProps();
        props.putAll(properties);

        Producer<String, String> producer = new KafkaProducer<>(props);
        producer.send(new ProducerRecord<String, String>(topic, key.toString(), value.toString()));

        producer.close();
    }

    public static String getTopic(){
        String topic = PropsUtil.loadProps("my.properties").getProperty("druidtopic");
        return topic;
    }

    public static Properties getProps(){
        Properties props = new Properties();
        props.put("bootstrap.servers",
                PropsUtil.loadProps("my.properties").getProperty("bootstrap.servers"));
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        return props;
    }
}
