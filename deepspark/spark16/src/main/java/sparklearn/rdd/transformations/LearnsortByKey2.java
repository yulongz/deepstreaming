/** 
 * Project Name:sparklearn 
 * File Name:LearnsortByKey2.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月6日下午4:27:25 
 * sky.zyl@hotmail.com
*/

package sparklearn.rdd.transformations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import scala.Tuple2;
import sparklearn.util.PrintUtilPro;

/**
 * ClassName:LearnsortByKey2 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月6日 下午4:27:25 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class LearnsortByKey2 {

	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setAppName(LearnsortByKey2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);

		List<Integer> datas = Arrays.asList(60, 70, 80, 55, 45, 75);

		JavaRDD<Integer> intRDD = sc.parallelize(datas).sortBy(new Function<Integer, Object>() {

			public Object call(Integer v1) throws Exception {
				return v1;
			}
		}, true, 1);

		PrintUtilPro.printList(intRDD.collect());

		// sc.parallelize(datas)
		// .sortBy((Integer v1) -> v1, false, 1)
		// .foreach(v -> System.out.println(v));

		List<Tuple2<Integer, Integer>> datas2 = new ArrayList<>();
		datas2.add(new Tuple2<>(3, 3));
		datas2.add(new Tuple2<>(2, 2));
		datas2.add(new Tuple2<>(1, 4));
		datas2.add(new Tuple2<>(2, 3));

		JavaPairRDD<Integer, Integer> pairRDD = sc.parallelizePairs(datas2).sortByKey(false);
		// pairRDD.foreach(v -> System.out.println(v));
		PrintUtilPro.printList(pairRDD.collect());
	}

}
