/** 
 * Project Name:sparklearn 
 * File Name:learnintersection.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月3日下午9:46:24 
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
 * ClassName:learnintersection <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年7月3日 下午9:46:24 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
public class Learnintersection {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("Learnintersection").setMaster("local");
		@SuppressWarnings("resource")
		JavaSparkContext jsc = new JavaSparkContext(conf);
		 
		List<String> list = new ArrayList<String>();
		list.add("11,22,33,44,55");
		list.add("aa,bb,cc,dd,ee");
		JavaRDD<String> jRDD = jsc.parallelize(list,1);
		 
		List<String> listN = new ArrayList<String>();
		listN.add("11,22,33,44,55");
		listN.add("AA,BB,CC,DD,EE");
		JavaRDD<String> jRDDN = jsc.parallelize(listN,1);
		 
		JavaRDD<String> jrintersection = jRDD.intersection(jRDDN);
		 
		PrintUtilPro.printList(jrintersection.collect());
	}

}
 