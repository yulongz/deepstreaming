/** 
 * Project Name:sparklearn 
 * File Name:Learnintersection2.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月6日下午3:50:14 
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
 * ClassName:Learnintersection2 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月6日 下午3:50:14 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class Learnintersection2 {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName(Learnintersection2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);

		List<String> datas1 = Arrays.asList("张三", "李四", "tom");
		List<String> datas2 = Arrays.asList("tom", "gim");

		JavaRDD<String> intersectionRDD = sc.parallelize(datas1).intersection(sc.parallelize(datas2));
		// intersectionRDD.foreach(v -> System.out.println(v));
		PrintUtilPro.printList(intersectionRDD.collect());
	}

}
