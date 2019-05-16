/** 
 * Project Name:sparklearn 
 * File Name:LearngroupByKey.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月3日下午9:49:00 
 * sky.zyl@hotmail.com
*/

package sparklearn.rdd.transformations;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;
import sparklearn.util.PrintUtilPro;

/**
 * ClassName:LearngroupByKey <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月3日 下午9:49:00 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class LearngroupByKey {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("LearngroupByKey").setMaster("local");
		@SuppressWarnings("resource")
		JavaSparkContext jsc = new JavaSparkContext(conf);
		List<String> list = new ArrayList<String>();
		list.add("11,22,33,44,55");
		list.add("aa,bb,cc,dd,ee");
		list.add("aa,bb,cc,dd,ee");
		JavaRDD<String> jRDD = jsc.parallelize(list, 1);
		// pairrdd
		JavaPairRDD<String, Integer> jPRDD = jRDD.mapToPair(new PairFunction<String, String, Integer>() {
			private static final long serialVersionUID = 1L;

			public Tuple2<String, Integer> call(String s) throws Exception {
				return new Tuple2<String, Integer>(s.split(",")[0], 1);
			}
		});

		JavaPairRDD<String, Iterable<Integer>> groupByKeyRDD = jPRDD.groupByKey();
		PrintUtilPro.printList(groupByKeyRDD.collect());
	}

}
