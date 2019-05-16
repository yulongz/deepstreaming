package context;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaKafkaWordCount {

    public static void main(String[] args) throws InterruptedException {
        boolean log4jInitialized = Logger.getRootLogger().getAllAppenders().hasMoreElements();
        org.slf4j.Logger logger = LoggerFactory.getLogger(JavaKafkaWordCount.class.getSimpleName());
        if (!log4jInitialized) {
            // We first log something to initialize Spark's default logging, then we override the
            // logging level.
            logger.info("Setting log level to [WARN] for streaming example." +
                    " To override add a custom log4j.properties to the classpath.");
            Logger.getRootLogger().setLevel(Level.WARN);
        }

        SparkConf sparkConf = new SparkConf().setAppName("context.JavaKafkaWordCount").setMaster("local");
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(2000));

        //rulechain:input->rule1->rule2->output

        JavaInputDStream<ConsumerRecord<Object, Object>> consumerRecordJavaInputDStream = fromKafka(jssc, InputRule.getContext("rule1"));

        consumerRecordJavaInputDStream.map(t->t.value().toString()).print();


        jssc.start();
        jssc.awaitTermination();

    }

    public static JavaInputDStream<ConsumerRecord<Object, Object>> fromKafka(JavaStreamingContext jssc,KafkaContext kafkaConext){

        JavaInputDStream<ConsumerRecord<Object, Object>> directStream =
                KafkaUtils.createDirectStream(jssc, LocationStrategies.PreferConsistent(), ConsumerStrategies.Subscribe(kafkaConext.getTopics(), kafkaConext.getKafkaParams()));

        return directStream;
    }

}
