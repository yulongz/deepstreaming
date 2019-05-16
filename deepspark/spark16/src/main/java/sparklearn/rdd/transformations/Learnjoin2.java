/** 
 * Project Name:sparklearn 
 * File Name:Learnjoin2.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月6日下午4:35:01 
 * sky.zyl@hotmail.com
*/

package sparklearn.rdd.transformations;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;
import sparklearn.util.PrintUtilPro;

/**
 * ClassName:Learnjoin2 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月6日 下午4:35:01 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class Learnjoin2 {

	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setAppName(Learnjoin2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);

		List<Tuple2<Integer, String>> products = new ArrayList<>();
		products.add(new Tuple2<>(1, "苹果"));
		products.add(new Tuple2<>(2, "梨"));
		products.add(new Tuple2<>(3, "香蕉"));
		products.add(new Tuple2<>(4, "石榴"));

		List<Tuple2<Integer, Integer>> counts = new ArrayList<>();
		counts.add(new Tuple2<>(1, 7));
		counts.add(new Tuple2<>(2, 3));
		counts.add(new Tuple2<>(3, 8));
		counts.add(new Tuple2<>(4, 3));
		counts.add(new Tuple2<>(5, 9));

		JavaPairRDD<Integer, String> productsRDD = sc.parallelizePairs(products);
		JavaPairRDD<Integer, Integer> countsRDD = sc.parallelizePairs(counts);

		JavaPairRDD<Integer, Tuple2<String, Integer>> joinRDD =
				(JavaPairRDD<Integer, Tuple2<String, Integer>>) productsRDD.join(countsRDD);
		// joinRDD.foreach(v -> System.out.println(v));
		PrintUtilPro.printList(joinRDD.collect());

	}

}
