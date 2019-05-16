/** 
 * Project Name:sparklearn 
 * File Name:LearnflatMap.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月3日下午9:42:24 
 * sky.zyl@hotmail.com
*/  
  
package sparklearn.rdd.transformations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;

import sparklearn.util.PrintUtilPro;

/** 
 * ClassName:LearnflatMap <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年7月3日 下午9:42:24 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
public class LearnflatMap {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("LearnflatMap").setMaster("local");
		@SuppressWarnings("resource")
		JavaSparkContext jsc = new JavaSparkContext(conf);
		 
		List<String> list = new ArrayList<String>();
		list.add("11,22,33,44,55");
		list.add("aa,bb,cc,dd,ee");
		JavaRDD<String> jRDD = jsc.parallelize(list);
		 
		JavaRDD<String> jrflatMap = jRDD.flatMap(new FlatMapFunction<String, String>() {
		    private static final long serialVersionUID = 1L;
		    public List<String> call(String v1) throws Exception {
		        return Arrays.asList(v1.split(","));
		    }
		});
		PrintUtilPro.printList(jrflatMap.collect());
	}

}
 