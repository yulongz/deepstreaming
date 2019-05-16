/** 
 * Project Name:sparklearn 
 * File Name:LearncountByKey2.java 
 * Package Name:sparklearn.rdd.action 
 * Date:2017年7月6日下午6:14:33 
 * sky.zyl@hotmail.com
*/  
  
package sparklearn.rdd.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;
import sparklearn.util.LocalFileUtil;

/** 
 * ClassName:LearncountByKey2 <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年7月6日 下午6:14:33 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
public class LearncountByKey2 {

	public static void main(String[] args) {
		

		SparkConf conf = new SparkConf().setAppName(LearnsaveAsObjectFile2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);
				
		List<Tuple2<Integer, Integer>> datas = new ArrayList<>();
		datas.add(new Tuple2<>(3, 3));
		datas.add(new Tuple2<>(2, 2));
		datas.add(new Tuple2<>(1, 4));
		datas.add(new Tuple2<>(2, 3));
		
		JavaPairRDD<Integer, Integer> pairRDD = sc.parallelizePairs(datas);
		
		System.out.println(pairRDD.countByKey());
		

	}

}
 