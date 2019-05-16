package example.kafka;

/**
 * Project Name:avrolearn
 * Package Name:example.kafka
 * Date:17-10-9 上午10:09
 * AUTHOR by zhangyulong
 * Email:sky.zyl@hotmail.com
 */
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AvroKafkaProducter {
    static Logger logger = LoggerFactory.getLogger("AvroKafkaProducter");
    public static final String USER_SCHEMA = "{"
            + "\"type\":\"record\","
            + "\"name\":\"Iteblog\","
            + "\"fields\":["
            + "  { \"name\":\"str1\", \"type\":\"string\" },"
            + "  { \"name\":\"str2\", \"type\":\"string\" },"
            + "  { \"name\":\"int1\", \"type\":\"int\" }"
            + "]}";

    public static void main(String[] args) throws InterruptedException, IOException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "breath:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(USER_SCHEMA);
        GenericDatumWriter genericDatumWriter = new GenericDatumWriter(schema);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        KafkaProducer<String, byte[]> producer = new KafkaProducer<String, byte[]>(props);

        for (int i = 0; i < 10; i++) {
            GenericData.Record avroRecord = new GenericData.Record(schema);
            avroRecord.put("str1", "Str 1-" + i);
            avroRecord.put("str2", "Str 2-" + i);
            avroRecord.put("int1", i);

            BinaryEncoder binaryEncoder = EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null);
            genericDatumWriter.write(avroRecord, binaryEncoder);
            binaryEncoder.flush();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.flush();

            ProducerRecord<String, byte[]> record = new ProducerRecord<String, byte[]>("avrotest", "" + i, bytes);
            producer.send(record);
            Thread.sleep(250);
            logger.info(String.valueOf(i));
        }

        producer.close();
    }
}
