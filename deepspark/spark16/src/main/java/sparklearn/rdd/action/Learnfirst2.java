/** 
 * Project Name:sparklearn 
 * File Name:Learnfirst2.java 
 * Package Name:sparklearn.rdd.action 
 * Date:2017年7月6日下午5:21:24 
 * sky.zyl@hotmail.com
*/  
  
package sparklearn.rdd.action;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

/** 
 * ClassName:Learnfirst2 <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年7月6日 下午5:21:24 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
public class Learnfirst2 {

	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setAppName(Learnfirst2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);
		
		List<Integer> number = Arrays.asList(1,2,3,4,5);
		
		Integer first = sc.parallelize(number).first();
		
		System.out.println(first);
		
	}

}
 