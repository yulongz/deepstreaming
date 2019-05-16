/** 
 * Project Name:sparklearn 
 * File Name:LearnflatMap2.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月4日下午5:20:16 
 * sky.zyl@hotmail.com
*/

package sparklearn.rdd.transformations;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.VoidFunction;

/**
 * ClassName:LearnflatMap2 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月4日 下午5:20:16 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class LearnflatMap2 {

	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setAppName(LearnflatMap2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);

		List<String> data = Arrays.asList("aa,bb,cc", "cxf,spring,struts2", "java,C++,javaScript");
		JavaRDD<String> rddData = sc.parallelize(data);
		JavaRDD<String> flatMapData = rddData.flatMap(
				// v -> Arrays.asList(v.split(",")).iterator() //jre1.8
				new FlatMapFunction<String, String>() {
					public Iterable<String> call(String t) throws Exception {
						List<String> list = Arrays.asList(t.split(","));
						return list;
					}
				});
		flatMapData.foreach(
				// v -> System.out.println(v) //jre1.8
				new VoidFunction<String>() {
					public void call(String string) throws Exception {
						System.out.println(string);
					}
				});

		sc.close();
	}

}
