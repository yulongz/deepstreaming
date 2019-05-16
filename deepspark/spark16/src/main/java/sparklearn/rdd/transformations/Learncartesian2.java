/** 
 * Project Name:sparklearn 
 * File Name:Learncartesian2.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月6日下午4:42:01 
 * sky.zyl@hotmail.com
*/

package sparklearn.rdd.transformations;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;

import scala.Tuple2;

/**
 * ClassName:Learncartesian2 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月6日 下午4:42:01 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class Learncartesian2 {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName(Learncartesian2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);

		List<String> names = Arrays.asList("张三", "李四", "王五");
		List<Integer> scores = Arrays.asList(60, 70, 80);

		JavaRDD<String> namesRDD = sc.parallelize(names);
		JavaRDD<Integer> scoreRDD = sc.parallelize(scores);

		JavaPairRDD<String, Integer> cartesianRDD = namesRDD.cartesian(scoreRDD);
		cartesianRDD.foreach(new VoidFunction<Tuple2<String, Integer>>() {

			private static final long serialVersionUID = 1L;

			public void call(Tuple2<String, Integer> t) throws Exception {
				System.out.println(t._1 + "\t" + t._2());
			}
		});

	}

}
