/** 
 * Project Name:sparklearn 
 * File Name:AccumulatorTest.java 
 * Package Name:sparklearn.rdd 
 * Date:2017年7月14日下午2:25:05 
 * sky.zyl@hotmail.com
*/

package sparklearn.rdd;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.broadcast.Broadcast;

import com.google.gson.Gson;

/**
 * ClassName:AccumulatorTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月14日 下午2:25:05 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class AccumulatorTest {

	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setAppName("AccumulatorTest").setMaster("local");

		JavaSparkContext jsc = new JavaSparkContext(conf);

		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);

	   Accumulator accum = jsc.accumulator(0, "My Accumulator");

	}

}
