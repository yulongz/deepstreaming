/** 
 * Project Name:sparklearn 
 * File Name:Learnunion2.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月6日下午3:48:31 
 * sky.zyl@hotmail.com
*/

package sparklearn.rdd.transformations;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import sparklearn.util.PrintUtilPro;

/**
 * ClassName:Learnunion2 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月6日 下午3:48:31 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class Learnunion2 {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName(Learnunion2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);

		List<String> datas1 = Arrays.asList("张三", "李四");
		List<String> datas2 = Arrays.asList("tom", "gim");

		JavaRDD<String> data1RDD = sc.parallelize(datas1);
		JavaRDD<String> data2RDD = sc.parallelize(datas2);

		JavaRDD<String> unionRDD = data1RDD.union(data2RDD);

		// unionRDD.foreach(v -> System.out.println(v));
		PrintUtilPro.printList(unionRDD.collect());

		sc.close();
	}

}
