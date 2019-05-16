/** 
 * Project Name:sparklearn 
 * File Name:Learnfilter.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月3日下午9:41:04 
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
 * ClassName:Learnfilter <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年7月3日 下午9:41:04 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
public class Learnfilter {
public static void main(String[] args) {
	SparkConf conf = new SparkConf().setAppName("Learnfilter").setMaster("local");
	@SuppressWarnings("resource")
	JavaSparkContext jsc = new JavaSparkContext(conf);
	 
	List<String> list = new ArrayList<String>();
	list.add("11,22,33,44,55");
	list.add("aa,bb,cc,dd,ee");
	JavaRDD<String> jRDD = jsc.parallelize(list);
	 
	JavaRDD<String> jrfilter = jRDD.filter(new Function<String, Boolean>() {
	    private static final long serialVersionUID = 1L;
	    public Boolean call(String v1) throws Exception {
	        if (v1.contains("a"))
	            return true;
	        return false;
	    }
	});
	PrintUtilPro.printList(jrfilter.collect());
}
}
 