/** 
 * Project Name:sparklearn 
 * File Name:LearnreduceByKey2.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月6日下午4:19:42 
 * sky.zyl@hotmail.com
*/

package sparklearn.rdd.transformations;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;

import scala.Tuple2;

/**
 * ClassName:LearnreduceByKey2 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月6日 下午4:19:42 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class LearnreduceByKey2 {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName(LearnreduceByKey2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);

		JavaRDD<String> lines = sc.textFile("data/README.md");

		JavaRDD<String> wordsRDD = lines.flatMap(new FlatMapFunction<String, String>() {

			private static final long serialVersionUID = 1L;

			public Iterable<String> call(String line) throws Exception {
				List<String> words = Arrays.asList(line.split(" "));
				return words;
			}
		});

		JavaPairRDD<String, Integer> wordsCount = wordsRDD.mapToPair(new PairFunction<String, String, Integer>() {

			private static final long serialVersionUID = 1L;

			public Tuple2<String, Integer> call(String word) throws Exception {
				return new Tuple2<String, Integer>(word, 1);
			}
		});

		JavaPairRDD<String, Integer> resultRDD = wordsCount.reduceByKey(new Function2<Integer, Integer, Integer>() {

			private static final long serialVersionUID = 1L;

			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1 + v2;
			}
		});

		resultRDD.foreach(new VoidFunction<Tuple2<String, Integer>>() {

			private static final long serialVersionUID = 1L;

			public void call(Tuple2<String, Integer> t) throws Exception {
				System.out.println(t._1 + "\t" + t._2());
			}
		});

		sc.close();

	}

}
