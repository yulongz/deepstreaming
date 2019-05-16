/** 
 * Project Name:sparklearn 
 * File Name:Learnsample2.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月6日下午3:46:33 
 * sky.zyl@hotmail.com
*/

package sparklearn.rdd.transformations;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import sparklearn.util.PrintUtilPro;

/**
 * ClassName:Learnsample2 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月6日 下午3:46:33 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class Learnsample2 {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName(Learnsample2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);

		List<Integer> datas = Arrays.asList(1, 2, 3, 7, 4, 5, 8);

		JavaRDD<Integer> dataRDD = sc.parallelize(datas);
		JavaRDD<Integer> sampleRDD = dataRDD.sample(false, 0.5, System.currentTimeMillis());
		//sampleRDD.foreach(v -> System.out.println(v));
		PrintUtilPro.printList(sampleRDD.collect());
		
		sc.close();
	}

}
