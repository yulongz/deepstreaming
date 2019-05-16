/** 
 * Project Name:sparklearn 
 * File Name:Learndistinct2.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月6日下午3:55:52 
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
 * ClassName:Learndistinct2 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月6日 下午3:55:52 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class Learndistinct2 {

	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setAppName(Learndistinct2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);

		List<String> datas = Arrays.asList("张三", "李四", "tom", "张三");

		JavaRDD<String> distinctRDD = sc.parallelize(datas).distinct();
		// distinctRDD.foreach(v -> System.out.println(v));
		
		PrintUtilPro.printList(distinctRDD.collect());

	}

}
