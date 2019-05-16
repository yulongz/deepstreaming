/** 
 * Project Name:sparklearn 
 * File Name:Learnsample.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月3日下午9:43:32 
 * sky.zyl@hotmail.com
*/

package sparklearn.rdd.transformations;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import sparklearn.util.PrintUtilPro;

/**
 * ClassName:Learnsample <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月3日 下午9:43:32 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class Learnsample {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("Learnsample").setMaster("local");
		@SuppressWarnings("resource")
		JavaSparkContext jsc = new JavaSparkContext(conf);
		List<String> list = new ArrayList<String>();
		list.add("11,22,33,44,55");
		list.add("aa,bb,cc,dd,ee");
		list.add("aa,bb,cc,dd,ee");
		JavaRDD<String> jRDD = jsc.parallelize(list, 1);

		JavaRDD<String> jrsample = jRDD.sample(false, 0.5, System.currentTimeMillis());

		PrintUtilPro.printList(jrsample.collect());
	}

}
