/** 
 * Project Name:sparklearn 
 * File Name:LearngroupByKey2.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月6日下午4:11:27 
 * sky.zyl@hotmail.com
*/

package sparklearn.rdd.transformations;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import sparklearn.util.PrintUtilPro;

/**
 * ClassName:LearngroupByKey2 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月6日 下午4:11:27 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class LearngroupByKey2 {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName(LearngroupByKey2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);

		List<Integer> datas = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

		JavaPairRDD<Object, Iterable<Integer>> intRDD = sc.parallelize(datas).groupBy(new Function<Integer, Object>() {

			public Object call(Integer v1) throws Exception {
				return (v1 % 2 == 0) ? "偶数" : "奇数";
			}
		});
		// intRDD.collect().forEach(System.out::println);
		PrintUtilPro.printList(intRDD.collect());

		List<String> datas2 = Arrays.asList("dog", "tiger", "lion", "cat", "spider", "eagle");

		JavaPairRDD<Object, Iterable<String>> strRDD = sc.parallelize(datas2).keyBy(new Function<String, Object>() {

			public Object call(String v1) throws Exception {
				return v1.length();
			}
		}).groupByKey();

		// strRDD.collect().forEach(System.out::println);
		PrintUtilPro.printList(strRDD.collect());

	}

}
