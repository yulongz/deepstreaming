/** 
 * Project Name:sparklearn 
 * File Name:Learnreduce2.java 
 * Package Name:sparklearn.rdd.action 
 * Date:2017年7月6日下午5:02:01 
 * sky.zyl@hotmail.com
*/

package sparklearn.rdd.action;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;

import sparklearn.rdd.transformations.LearnrepartitionAndSortWithinPartitions2;

/**
 * ClassName:Learnreduce2 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月6日 下午5:02:01 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class Learnreduce2 {

	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setAppName(Learnreduce2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);
		
		List<Integer> number = Arrays.asList(1,2,3,4,5);
		
		Integer numberRDD = sc.parallelize(number).reduce(new Function2<Integer,Integer,Integer>(){

			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1+v2;
			}
		});
		
		System.out.println(numberRDD);

	}

}
