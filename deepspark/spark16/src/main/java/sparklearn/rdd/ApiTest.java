/** 
 * Project Name:sparklearn 
 * File Name:ApiTest.java 
 * Package Name:sparklearn.rdd 
 * Date:2017年7月3日下午5:47:54 
 * sky.zyl@hotmail.com
*/

package sparklearn.rdd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;
import sparklearn.util.PrintUtilPro;

/**
 * ClassName:ApiTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月3日 下午5:47:54 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class ApiTest {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("JavaApiLearn").setMaster("local");
		@SuppressWarnings("resource")
		JavaSparkContext jsc = new JavaSparkContext(conf);

		List<String> list = new ArrayList<>();
		list.add("11,22,33,44,55");
		list.add("aa,bb,cc,dd,ee");
		list.add("aa,bb,cc,dd,ee");
		JavaRDD<String> jRDD = jsc.parallelize(list, 1);

		// pairrdd
		JavaPairRDD<String, Integer> jPRDD = jRDD.mapToPair(new PairFunction<String, String, Integer>() {

			private static final long serialVersionUID = 1L;

			public Tuple2<String, Integer> call(String s) throws Exception {
				return new Tuple2<>(s.split(",")[0], 1);
			}
		});
		
		JavaPairRDD<String, Integer> jPRDDCopy = jPRDD;

		JavaPairRDD<String, Tuple2<Iterable<Integer>, Iterable<Integer>>> cogroupRDD =
				(JavaPairRDD<String, Tuple2<Iterable<Integer>, Iterable<Integer>>>) jPRDD.cogroup(jPRDDCopy);

		PrintUtilPro.printList(cogroupRDD.collect());
		
		// List<String> listN = new ArrayList<String>();
		// listN.add("11,22,33,44,55");
		// listN.add("AA,BB,CC,DD,EE");
		// JavaRDD<String> jRDDN = jsc.parallelize(listN,1);
		//
		// JavaRDD<String> jrsubtract = jRDD.subtract(jRDDN);
		//
		// PrintUtilPro.printList(jrsubtract.collect());

	}

}
