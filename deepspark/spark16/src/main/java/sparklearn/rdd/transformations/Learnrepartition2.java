/** 
 * Project Name:sparklearn 
 * File Name:Learnrepartition2.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月6日下午4:54:15 
 * sky.zyl@hotmail.com
*/

package sparklearn.rdd.transformations;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * ClassName:Learnrepartition2 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月6日 下午4:54:15 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class Learnrepartition2 {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName(Learnrepartition2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);

		List<String> datas = Arrays.asList("hi", "hello", "how", "are", "you");
		JavaRDD<String> datasRDD = sc.parallelize(datas, 4);
		System.out.println("RDD的分区数: " + datasRDD.partitions().size());
		JavaRDD<String> datasRDD2 = datasRDD.repartition(2);  //repartition(num) = coaleasce(num,true)
		System.out.println("RDD的分区数: " + datasRDD2.partitions().size());

	}

}
