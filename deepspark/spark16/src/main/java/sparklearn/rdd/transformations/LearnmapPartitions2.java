/** 
 * Project Name:sparklearn 
 * File Name:LearnmapPartitions.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月4日下午5:26:58 
 * sky.zyl@hotmail.com
*/

package sparklearn.rdd.transformations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;

import sparklearn.util.PrintUtilPro;

/**
 * ClassName:LearnmapPartitions <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月4日 下午5:26:58 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class LearnmapPartitions2 {

	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setAppName(LearnmapPartitions2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);

		List<String> names = Arrays.asList("张三1", "李四1", "王五1", "张三2", "李四2", "王五2", "张三3", "李四3", "王五3", "张三4");

		JavaRDD<String> namesRDD = sc.parallelize(names, 3);
		JavaRDD<String> mapPartitionsRDD = namesRDD.mapPartitions(new FlatMapFunction<Iterator<String>, String>() {
			int count = 0;
			public Iterable<String> call(Iterator<String> stringIterator) throws Exception {
				List<String> list = new ArrayList<String>();
				while (stringIterator.hasNext()) {
					list.add("分区索引:" + count++ + "\t" + stringIterator.next());
				}
				return list;
			}
		});

		// 从集群获取数据到本地内存中
		List<String> result = mapPartitionsRDD.collect();
		PrintUtilPro.printList(result);

		sc.close();
	}

}
