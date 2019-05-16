package context;

import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class KafkaContext {

    Map<String,Object> kafkaParams;

    HashSet<String> topics;

    public KafkaContext(Map<String, Object> kafkaParams, HashSet<String> topics) {
        this.kafkaParams = kafkaParams;
        this.topics = topics;
    }

    public Map<String, Object> getKafkaParams() {
        return kafkaParams;
    }

    public void setKafkaParams(Map<String, Object> kafkaParams) {
        this.kafkaParams = kafkaParams;
    }

    public HashSet<String> getTopics() {
        return topics;
    }

    public void setTopics(HashSet<String> topics) {
        this.topics = topics;
    }
}
