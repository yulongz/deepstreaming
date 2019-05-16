/** 
 * Project Name:sparklearn 
 * File Name:Learncogroup2.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月6日下午4:37:30 
 * sky.zyl@hotmail.com
*/

package sparklearn.rdd.transformations;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;
import scala.Tuple3;
import sparklearn.util.PrintUtilPro;

/**
 * ClassName:Learncogroup2 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月6日 下午4:37:30 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class Learncogroup2 {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName(Learncogroup2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);

		List<Tuple2<Integer, String>> datas1 = new ArrayList<>();
		datas1.add(new Tuple2<>(1, "苹果"));
		datas1.add(new Tuple2<>(2, "梨"));
		datas1.add(new Tuple2<>(3, "香蕉"));
		datas1.add(new Tuple2<>(4, "石榴"));

		List<Tuple2<Integer, Integer>> datas2 = new ArrayList<>();
		datas2.add(new Tuple2<>(1, 7));
		datas2.add(new Tuple2<>(2, 3));
		datas2.add(new Tuple2<>(3, 8));
		datas2.add(new Tuple2<>(4, 3));

		List<Tuple2<Integer, String>> datas3 = new ArrayList<>();
		datas3.add(new Tuple2<>(1, "7"));
		datas3.add(new Tuple2<>(2, "3"));
		datas3.add(new Tuple2<>(3, "8"));
		datas3.add(new Tuple2<>(4, "3"));
		datas3.add(new Tuple2<>(4, "4"));
		datas3.add(new Tuple2<>(4, "5"));
		datas3.add(new Tuple2<>(4, "6"));

		JavaPairRDD<Integer, Tuple3<Iterable<String>, Iterable<Integer>, Iterable<String>>> cogroupRDD =
                (JavaPairRDD<Integer, Tuple3<Iterable<String>, Iterable<Integer>, Iterable<String>>>)
                        sc.parallelizePairs(datas1).cogroup(sc.parallelizePairs(datas2), sc.parallelizePairs(datas3));
		//cogroupRDD.foreach(v -> System.out.println(v));
		PrintUtilPro.printList(cogroupRDD.collect());

	}

}
