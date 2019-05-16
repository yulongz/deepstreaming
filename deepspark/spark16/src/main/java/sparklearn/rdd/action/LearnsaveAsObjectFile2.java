/** 
 * Project Name:sparklearn 
 * File Name:LearnsaveAsObjectFile2.java 
 * Package Name:sparklearn.rdd.action 
 * Date:2017年7月6日下午6:13:06 
 * sky.zyl@hotmail.com
*/  
  
package sparklearn.rdd.action;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import sparklearn.util.LocalFileUtil;

/** 
 * ClassName:LearnsaveAsObjectFile2 <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年7月6日 下午6:13:06 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
public class LearnsaveAsObjectFile2 {

	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setAppName(LearnsaveAsObjectFile2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);
		
		List<Integer> number = Arrays.asList(1,2,3,4,5);
		
		LocalFileUtil.delDir("data/saveAsObjectFile");
		
		sc.parallelize(number).saveAsObjectFile("data/saveAsObjectFile");
	}

}
 