/** 
 * Project Name:sparklearn 
 * File Name:Learnmap.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月3日下午9:39:44 
 * sky.zyl@hotmail.com
*/  
  
package sparklearn.rdd.transformations;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import sparklearn.util.PrintUtilPro;

/** 
 * ClassName:Learnmap <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年7月3日 下午9:39:44 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
public class Learnmap {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("Learnmap").setMaster("local");
		@SuppressWarnings("resource")
		JavaSparkContext jsc = new JavaSparkContext(conf);
		 
		List<String> list = new ArrayList<String>();
		list.add("11,22,33,44,55");
		list.add("aa,bb,cc,dd,ee");
		JavaRDD<String> jRDD = jsc.parallelize(list);
		 
		JavaRDD<String[]> jrmap = jRDD.map(new Function<String, String[]>() {
		    private static final long serialVersionUID = 1L;
		    public String[] call(String v1) throws Exception {
		        return v1.split(",");
		    }
		});
		 
		PrintUtilPro.printList(jrmap.collect());
	}

}
 