/** 
 * Project Name:sparklearn 
 * File Name:LearntakeSample2.java 
 * Package Name:sparklearn.rdd.action 
 * Date:2017年7月6日下午5:25:11 
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
 * ClassName:LearntakeSample2 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月6日 下午5:25:11 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class LearntakeSample2 {

	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setAppName(Learnfirst2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);

		List<Integer> datas = Arrays.asList(1, 2, 3, 7, 4, 5, 8);

		JavaRDD<Integer> dataRDD = sc.parallelize(datas);

		PrintUtilPro.printList(dataRDD.takeSample(false, 3, System.currentTimeMillis()));

		sc.close();

	}

}
