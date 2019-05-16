package context;

import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class InputRule {

    public static KafkaContext getContext(String ruleId){

        Map<String,Object> kafkaParams = new HashMap<String,Object>();
        kafkaParams.put("group.id","zylroup");
        kafkaParams.put("bootstrap.servers","172.16.13.31:9092,172.16.13.32:9092,172.16.13.33:9092");
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("auto.offset.reset", "earliest");
        //kafkaParams.put("enable.auto.commit", false);

        HashSet<String> topics = new HashSet<>();
        topics.add("test");


        return new KafkaContext(kafkaParams,topics);
    }



}
