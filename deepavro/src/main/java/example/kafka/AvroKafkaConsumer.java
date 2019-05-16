package example.kafka;

/**
 * Project Name:avrolearn
 * Package Name:example.kafka
 * Date:17-10-9 上午11:03
 * AUTHOR by zhangyulong
 * Email:sky.zyl@hotmail.com
 */

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AvroKafkaConsumer {
    static Logger logger = LoggerFactory.getLogger("AvroKafkaConsumer");
    private final ConsumerConnector consumer;
    private final String topic;

    public AvroKafkaConsumer(String zookeeper, String groupId, String topic) {
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeper);
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", "500");
        props.put("zookeeper.sync.time.ms", "250");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "smallest");

        consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
        this.topic = topic;
    }

    public void testConsumer() throws IOException {
        Map<String, Integer> topicCount = new HashMap<String, Integer>();
        topicCount.put(topic, 1);

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumer.createMessageStreams(topicCount);
        List<KafkaStream<byte[], byte[]>> streams = consumerStreams.get(topic);

        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(AvroKafkaProducter.USER_SCHEMA);
        GenericDatumReader genericDatumReader = new GenericDatumReader(schema);
        for (final KafkaStream stream : streams) {
            ConsumerIterator it = stream.iterator();
            while (it.hasNext()) {
                MessageAndMetadata messageAndMetadata = it.next();
                String key = new String((byte[]) messageAndMetadata.key());
                byte[] message = (byte[]) messageAndMetadata.message();

                ByteArrayInputStream byteArrayInputStream =  new ByteArrayInputStream(message);
                BinaryDecoder binaryDecoder = DecoderFactory.get().binaryDecoder(byteArrayInputStream, null);
                GenericData.Record avroRecord = new GenericData.Record(schema);
                GenericData.Record record = (GenericData.Record) genericDatumReader.read(avroRecord, binaryDecoder);

                logger.info("key=" + key + ", str1= " + record.get("str1")
                        + ", str2= " + record.get("str2")
                        + ", int1=" + record.get("int1"));
            }
        }
        consumer.shutdown();
    }

    public static void main(String[] args) throws IOException {
        AvroKafkaConsumer simpleConsumer =
                new AvroKafkaConsumer("breath:2181/kafka01021", "testgroup", "avrotest");
        simpleConsumer.testConsumer();
    }
}