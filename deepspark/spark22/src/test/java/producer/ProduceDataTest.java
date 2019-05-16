package producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import utils.PropsUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 * @author Tiny
 * @date 2017-11-13
 * <p>
 * 发送原始JSON数据
 */
public class ProduceDataTest {

    @Test
    public void produceDataTest() throws InterruptedException, IOException {
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


        String path = "data/sourcedata.txt";

        FileWriter fw = new FileWriter(path);

        String sourceTopic = PropsUtil.loadProps("my.properties").getProperty("sourcetopic");

        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 100; i++) {
            producer.send(new ProducerRecord<String, String>(sourceTopic, Integer.toString(i), sourceData()));

            fw.write(sourceData() + "\r\n");

            Thread.sleep(1000);
        }

        fw.close();
        producer.close();
    }

    public static String randDouble() {
        DecimalFormat df = new DecimalFormat("###0.00");
        double min = 150d;
        double max = 300d;
        double floatBounded = min + new Random().nextDouble() * (max - min);
        if (floatBounded < 200d) {
            floatBounded = 0;
        }
        return df.format(floatBounded);
    }

    public static String sourceData() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String dateString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date);

        String data = "{" +
                "\"UTCTime\":" + "\"" + dateString + "\"," +
                "\"UnixTime\":" + "\"" + System.currentTimeMillis() + "\"," +
                "\"DCS:AI:10HLF10CF101\":" + "\"" + randDouble() + "\"," +
                "\"DCS:AI:10HLF10CF101F\":" + "\"" + randDouble() + "\"," +
                "\"DCS:AI:10HLF10CF102\":" + "\"" + randDouble() + "\"," +
                "\"DCS:AI:10HLF10CF102F\":" + "\"" + randDouble() + "\"," +
                "\"DCS:AI:10HLF10CF103\":" + "\"" + randDouble() + "\"," +
                "\"DCS:AI:10HLF10CF103F\":" + "\"" + randDouble() + "\"," +
                "\"DCS:AI:10HLF20CF101\":" + "\"" + randDouble() + "\"," +
                "\"DCS:AI:10HLF20CF101F\":" + "\"" + randDouble() + "\"," +
                "\"DCS:AI:10HLF20CF102\":" + "\"" + randDouble() + "\"," +
                "\"DCS:AI:10HLF20CF102F\":" + "\"" + randDouble() + "\"," +
                "\"DCS:AI:10HLF20CF103\":" + "\"" + randDouble() + "\"," +
                "\"DCS:AI:10HLF20CF103F\":" + "\"" + randDouble() + "\"" +
                "}";

        return data;
    }
}
