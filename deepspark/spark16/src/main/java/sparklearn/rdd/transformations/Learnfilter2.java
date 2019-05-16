/** 
 * Project Name:sparklearn 
 * File Name:Learnfilter2.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月4日下午5:12:50 
 * sky.zyl@hotmail.com
*/

package sparklearn.rdd.transformations;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;

/**
 * ClassName:Learnfilter2 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月4日 下午5:12:50 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class Learnfilter2 {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName(Learnfilter2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);

		List<Integer> datas = Arrays.asList(1, 2, 3, 7, 4, 5, 8);

		JavaRDD<Integer> rddData = sc.parallelize(datas);
		JavaRDD<Integer> filterRDD = rddData.filter(
				// jdk1.8
				// v1 -> v1 >= 3
				new Function<Integer, Boolean>() {
					public Boolean call(Integer v) throws Exception {
						return v >= 3;
					}
				});
		filterRDD.foreach(
				// jdk1.8
				// v -> System.out.println(v)
				new VoidFunction<Integer>() {
					public void call(Integer integer) throws Exception {
						System.out.println(integer);
					}
				});
		sc.close();
	}

}
