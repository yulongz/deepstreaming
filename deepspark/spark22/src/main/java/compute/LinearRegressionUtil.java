package compute;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.mllib.regression.LinearRegressionWithSGD;
import scala.Tuple2;

import java.util.List;

import static java.lang.Double.parseDouble;

/**
 * @author zhangyulong
 */
public class LinearRegressionUtil {
    public static LinearRegressionModel getModel(JavaSparkContext sc,JavaRDD<String[]> javaRDD){
        JavaRDD<LabeledPoint> labeledPoint = javaRDD.map((Function<String[], LabeledPoint>) v1 -> {
            LabeledPoint labeledPoint1 = new LabeledPoint(parseDouble(v1[0]),
                    Vectors.dense(1, parseDouble(v1[1])));
            return labeledPoint1;
        });
        LinearRegressionModel model = LinearRegressionWithSGD.train(labeledPoint.rdd(), 1000, 0.0001);
        return model;
    }

    public static double getPredict(LinearRegressionModel model,double input){
        double predict = model.predict(Vectors.dense(1, input));
        return predict;
    }
}
