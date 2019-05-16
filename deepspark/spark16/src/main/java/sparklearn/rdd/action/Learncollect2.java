/** 
 * Project Name:sparklearn 
 * File Name:Learncollect2.java 
 * Package Name:sparklearn.rdd.action 
 * Date:2017年7月6日下午5:11:17 
 * sky.zyl@hotmail.com
*/  
  
package sparklearn.rdd.action;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import sparklearn.util.PrintUtilPro;

/** 
 * ClassName:Learncollect2 <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年7月6日 下午5:11:17 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
public class Learncollect2 {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName(Learncollect2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);
		
		List<Integer> number = Arrays.asList(1,2,3,4,5);
		
		JavaRDD<Integer> numRDD = sc.parallelize(number);
		
		System.out.println(numRDD.count());
		
	}

}
 