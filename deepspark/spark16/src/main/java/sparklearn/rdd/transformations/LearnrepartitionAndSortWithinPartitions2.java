/** 
 * Project Name:sparklearn 
 * File Name:LearnrepartitionAndSortWithinPartitions2.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月6日下午4:56:34 
 * sky.zyl@hotmail.com
*/

package sparklearn.rdd.transformations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.spark.Partitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;
import sparklearn.util.PrintUtilPro;

/**
 * ClassName:LearnrepartitionAndSortWithinPartitions2 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月6日 下午4:56:34 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class LearnrepartitionAndSortWithinPartitions2 {

	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setAppName(LearnrepartitionAndSortWithinPartitions2.class.getSimpleName())
				.setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);

		List<String> datas = new ArrayList<>();
		Random random = new Random(1);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 100; j++) {
				datas.add(String.format("product%02d,url%03d", random.nextInt(10), random.nextInt(100)));
			}
		}
		JavaRDD<String> datasRDD = sc.parallelize(datas);
		JavaPairRDD<String, String> pairRDD = datasRDD.mapToPair(new PairFunction<String, String, String>() {
			private static final long serialVersionUID = 1L;

			public Tuple2<String, String> call(String s) throws Exception {
				return new Tuple2<String, String>(s.split(",")[0], s.split(",")[1]);
			}
		});

		// String v) -> {
		// String[] values = v.split(",");
		// return new Tuple2<>(values[0], values[1]);
		// });
		JavaPairRDD<String, String> partSortRDD = pairRDD.repartitionAndSortWithinPartitions(new Partitioner() {

			@Override
			public int numPartitions() {
				return 10;
			}

			@Override
			public int getPartition(Object key) {
				return Integer.valueOf(((String) key).substring(7));
			}
		});
		//partSortRDD.collect().forEach(System.out::println);
		PrintUtilPro.printList(partSortRDD.collect());
	}

}
