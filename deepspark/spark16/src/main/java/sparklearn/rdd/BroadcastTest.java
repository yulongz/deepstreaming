/** 
 * Project Name:sparklearn 
 * File Name:BroadcastTest.java 
 * Package Name:sparklearn.rdd 
 * Date:2017年7月14日下午2:19:49 
 * sky.zyl@hotmail.com
*/  
  
package sparklearn.rdd;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;

/** 
 * ClassName:BroadcastTest <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年7月14日 下午2:19:49 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
public class BroadcastTest {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("BroadcastTest").setMaster("local");

		JavaSparkContext jsc = new JavaSparkContext(conf);
		
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		
		Broadcast<List<Integer>> broadcast = jsc.broadcast(list);
		System.out.println(broadcast.getValue().toString());
		
	}

}
 