package compute;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.mllib.regression.LinearRegressionWithSGD;
import org.junit.Test;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;

public class LinearRegressionUtilTest {

    //@Test
    public void linearRegressionTest() {
        SparkConf sparkConf = new SparkConf().setAppName("mlib").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        JavaRDD<String> stringJavaRDD = sc.textFile("data/prices_LinearRegression.txt", 1);
        JavaRDD<String[]> map = stringJavaRDD.map((Function<String, String[]>) v1 -> v1.split("\\s+"));
        JavaRDD<LabeledPoint> labeledPoint = map.map(new Function<String[], LabeledPoint>() {
            @Override
            public LabeledPoint call(String[] v1) throws Exception {
                LabeledPoint labeledPoint = new LabeledPoint(parseDouble(v1[0]),
                        Vectors.dense(parseDouble(v1[1]), parseDouble(v1[2])));
                return labeledPoint;
            }
        });

        //创建模型
        LinearRegressionModel model = LinearRegressionWithSGD.train(labeledPoint.rdd(), 1000, 0.0001);

        //得到权重
        System.out.println("得到权重:" + model.weights());

        //样本数据进行对比
        JavaRDD<Double> prediction = model.predict(labeledPoint.map(t -> t.features()));
        JavaPairRDD<Double, Double> predictionAndLabel = prediction.zip(labeledPoint.map(t -> t.label()));
        List<Tuple2<Double, Double>> print_predict = predictionAndLabel.take(50);
        print_predict.forEach(t -> System.out.println(t._1 + "\t" + t._2));

        //房价预测，100平和200平
        System.out.println("预测100平:" + model.predict(Vectors.dense(1, 100)));
        System.out.println("预测200平:" + model.predict(Vectors.dense(1, 200)));

        //误差计算
        Double loss = predictionAndLabel.map(t -> (t._1() - t._2()) * (t._1() - t._2())).reduce((Function2<Double, Double, Double>) (v1, v2) -> v1 + v2);
        double sqrt = Math.sqrt(loss / labeledPoint.count());
        System.out.println("Test rmse=" + sqrt);

        //模型保存
        String ModelPath = "data/LinearRegressionModel";
        model.save(sc.sc(), ModelPath);
        LinearRegressionModel sameModel = LinearRegressionModel.load(sc.sc(), ModelPath);
        System.out.println(sameModel.weights());
    }

    @Test
    public void listparallelize() {
        SparkConf sparkConf = new SparkConf().setAppName("mlib").setMaster("local");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        List<String[]> listTR = new ArrayList<String[]>();

        listTR.add(new String[]{"123","246"});
        listTR.add(new String[]{"124","248"});
        listTR.add(new String[]{"125","250"});

        JavaRDD<String[]> parallelizeTR = jsc.parallelize(listTR);
        LinearRegressionModel model = LinearRegressionUtil.getModel(jsc, parallelizeTR);

        System.out.println(LinearRegressionUtil.getPredict(model,244));
    }

}
