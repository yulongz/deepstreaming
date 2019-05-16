package iot;

import com.alibaba.fastjson.JSONObject;
import compute.MathLinearRegressionUtil;
import context.JavaKafkaWordCount;
import hbase.StoreData;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.slf4j.LoggerFactory;
import scala.Tuple2;
import utils.*;

import java.util.*;

/**
 * @author zhangyulong
 */
public class IotExample {
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

        SparkConf sparkConf = new SparkConf().setAppName("context.JavaKafkaWordCount").setMaster("local[4]");
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(1000));
        /*JavaSparkContext jsc = jssc.sparkContext();*/


        Properties properties = PropsUtil.loadProps("my.properties");
        String sourceTopic = PropsUtil.getString(properties, "sourcetopic");
        String groupId = PropsUtil.getString(properties, "group.id");
        String bootstrapServers = PropsUtil.getString(properties, "bootstrap.servers");
        String mappingTable = PropsUtil.getString(properties, "mapping.table");

        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("group.id", groupId);
        kafkaParams.put("bootstrap.servers", bootstrapServers);
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("auto.offset.reset", "earliest");

        HashSet<String> topics = new HashSet<>();
        topics.add(sourceTopic);

        JavaInputDStream<ConsumerRecord<Object, Object>> directStream =
                KafkaUtils.createDirectStream(jssc, LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.Subscribe(topics, kafkaParams));

        /*将源编码替换为标准化编码*/
        JavaDStream<JSONObject> javaDStream =
                directStream.map(t -> t.value().toString()).map(t -> JsonUtil.fromJson(t))
                        .map(t -> JsonUtil.transformKeys(t, mappingTable));

        /*拆分成keyvalue(时间戳:测点列表)->拆分result为多条数据*/
        JavaPairDStream<JSONObject, JSONObject> jsonObjectJSONObjectJavaPairDStream =
                javaDStream.mapToPair(t -> JsonUtil.splitJSONObject(t)).flatMapValues(t -> JsonUtil.valuesToList(t));

        /*转换Key:value*/
        JavaDStream<Tuple2<String, JSONObject>> result =
                jsonObjectJSONObjectJavaPairDStream.map((Function<Tuple2<JSONObject, JSONObject>, Tuple2<String, JSONObject>>) v1 ->
                        {
                            Iterator<String> iterator = v1._2.keySet().iterator();
                            String newKey = iterator.next();
                            JSONObject newValue = new JSONObject();
                            newValue.putAll(v1._1);
                            newValue.put("result", v1._2.get(newKey));
                            return new Tuple2<>(newKey, newValue);
                        });

        /*keyvalue(UnixTime:JSONObject)*/
        JavaDStream<Tuple2<String, JSONObject>> unixTime =
                result.map((Function<Tuple2<String, JSONObject>, Tuple2<String, JSONObject>>) v1 ->
                {
                    String newKey = v1._1;
                    Object valueKey = v1._2.get("UnixTime");
                    JSONObject valueValue = v1._2;
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(valueKey.toString(), valueValue);
                    return new Tuple2<>(newKey, jsonObject);
                });

        /*转换成keyvalue*/
        JavaPairDStream<String, JSONObject> stringJSONObjectJavaPairDStream =
                unixTime.mapToPair((PairFunction<Tuple2<String, JSONObject>, String, JSONObject>) stringJSONObjectTuple2 ->
                        new Tuple2<>(stringJSONObjectTuple2._1, stringJSONObjectTuple2._2));

        /*5秒时间窗*/
        JavaPairDStream<String, JSONObject> stringJSONObjectJavaPairDStream1 =
                stringJSONObjectJavaPairDStream.reduceByKeyAndWindow((Function2<JSONObject, JSONObject, JSONObject>) (v1, v2) ->
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.putAll(v1);
                    jsonObject.putAll(v2);
                    return jsonObject;
                }, Durations.seconds(5), Durations.seconds(5));

        /*生成aggreaget标签*/
        JavaPairDStream<String, JSONObject> stringJSONObjectJavaPairDStream2 =
                stringJSONObjectJavaPairDStream1.mapValues((Function<JSONObject, JSONObject>) (JSONObject v1) ->
                {
                    List<String> list = new ArrayList<>();
                    HashMap modelDataMap = new HashMap();
                    for (Map.Entry<String, Object> m : v1.entrySet()) {
                        String resultValue = (String) ((JSONObject) m.getValue()).get("result");
                        list.add(resultValue);
                        //模型数据
                        Double doubleKey = Double.parseDouble(m.getKey());
                        Double doubleValue = Double.parseDouble(resultValue);
                        //空值数据不做模型训练
                        if (doubleValue != 0) {
                            modelDataMap.put(doubleKey, doubleValue);
                        }
                    }
                    JSONObject jsonObjectAgg = new JSONObject();
                    jsonObjectAgg.put("sum", FunctionUtil.sum(list));
                    jsonObjectAgg.put("count", FunctionUtil.count(list));
                    jsonObjectAgg.put("countNotNull", FunctionUtil.countNotNull(list));
                    jsonObjectAgg.put("avg", FunctionUtil.avg(list));
                    jsonObjectAgg.put("avgNotNull", FunctionUtil.avgNotNull(list));
                    jsonObjectAgg.put("max", FunctionUtil.max(list));
                    jsonObjectAgg.put("maxNotNull", FunctionUtil.maxNotNull(list));
                    jsonObjectAgg.put("min", FunctionUtil.min(list));
                    jsonObjectAgg.put("minNotNull", FunctionUtil.minNotNull(list));

            /*模型训练*/
                    Object[][] doubles = MapUtil.mapToArray(modelDataMap);
                    double[][] mathdoubles = ArrayUtil.DoubleTodouble(doubles);
                    SimpleRegression mathLinearRegression = MathLinearRegressionUtil.getMathLinearRegression(mathdoubles);
                    jsonObjectAgg.put("SimpleRegression", mathLinearRegression);

                    //返回值
                    JSONObject jsonObjectNew = new JSONObject();
                    for (Map.Entry<String, Object> m : v1.entrySet()) {
                        JSONObject jsonObjectValue = (JSONObject) m.getValue();

                        String resultValue = (String) jsonObjectValue.get("result");
                        Double doubleKey = Double.parseDouble(m.getKey());
                        Double doubleValue = Double.parseDouble(resultValue);
                        String digit = resultValue;
                        if (doubleValue == 0) {
                            double mathLinearRegressionPredict =
                                    MathLinearRegressionUtil.getMathLinearRegressionPredict(mathLinearRegression, doubleKey);
                            digit = FunctionUtil.digit(mathLinearRegressionPredict, 2);
                        }
                        jsonObjectValue.put("result", digit);
                        jsonObjectNew.put(m.getKey(), jsonObjectValue);
                    }
                    //jsonObjectNew.put("aggregation",jsonObjectAgg);

                    return jsonObjectNew;
                });

        JavaPairDStream<String, JSONObject> stringJSONObjectJavaPairDStream3 =
                stringJSONObjectJavaPairDStream2.flatMapValues(t -> JsonUtil.valuesToList(t));

        JavaPairDStream<String, JSONObject> stringJSONObjectJavaPairDStream4 =
                stringJSONObjectJavaPairDStream3.map((Function<Tuple2<String, JSONObject>, Tuple2<String, JSONObject>>) v1 ->
                {
                    Iterator<String> iterator = v1._2.keySet().iterator();
                    String newKey = iterator.next();
                    JSONObject newValue = new JSONObject();
                    newValue.put(v1._1, v1._2.get(newKey));
                    return new Tuple2<>(newKey, newValue);
                }).mapToPair((PairFunction<Tuple2<String, JSONObject>, String, JSONObject>) tuple2 ->
                        new Tuple2<>(tuple2._1, tuple2._2));

        JavaPairDStream<String, JSONObject> result1 =
                stringJSONObjectJavaPairDStream4.reduceByKey((Function2<JSONObject, JSONObject, JSONObject>) (v1, v2) ->
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.putAll(v1);
                    jsonObject.putAll(v2);
                    return jsonObject;
                }).mapValues((Function<JSONObject, JSONObject>) v1 ->
                {
                    JSONObject jsonObject = new JSONObject();
                    for (Map.Entry<String, Object> m : v1.entrySet()) {
                        String measureKey = m.getKey();
                        String resultValue = (String) ((JSONObject) m.getValue()).get("result");
                        jsonObject.put(measureKey, resultValue);
                    }
                    return jsonObject;
                });

        JavaDStream<JSONObject> unixTimeToHbase = result1.map((Function<Tuple2<String, JSONObject>, JSONObject>) v1 ->
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UnixTime", Long.parseLong(v1._1));
            jsonObject.putAll(v1._2);
            return jsonObject;
        });

        //固化hbase
        unixTimeToHbase.foreachRDD((VoidFunction<JavaRDD<JSONObject>>) jsonObjectJavaRDD ->
                jsonObjectJavaRDD.foreachPartition((VoidFunction<Iterator<JSONObject>>) jsonObjectIterator ->
                {
                    while (jsonObjectIterator.hasNext()) {
                        JSONObject next = jsonObjectIterator.next();
                        StoreData.storeData(JsonUtil.toJson(next), "yulongz-interp");
                    }
                }));

        JavaDStream<JSONObject> UTCTimeToKafka = result1.map((Function<Tuple2<String, JSONObject>, JSONObject>) v1 ->
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UTCTime", TimeUtil.unix2Utc(Long.parseLong(v1._1)));
            jsonObject.putAll(v1._2);
            return jsonObject;
        });

        //推入Kafka
        UTCTimeToKafka.foreachRDD((VoidFunction<JavaRDD<JSONObject>>) jsonObjectJavaRDD ->
                jsonObjectJavaRDD.foreachPartition((VoidFunction<Iterator<JSONObject>>) jsonObjectIterator ->
                {
                    while (jsonObjectIterator.hasNext()) {
                        JSONObject next = jsonObjectIterator.next();
                        KafkaUtil.produceData("zyl-wotopc", "", next.toJSONString());
                    }
                }));

        jssc.start();
        jssc.awaitTermination();
    }
}
