/** 
 * Project Name:sparklearn 
 * File Name:Learnpipe2.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月6日下午4:44:48 
 * sky.zyl@hotmail.com
*/  
  
package sparklearn.rdd.transformations;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import sparklearn.util.PrintUtilPro;

/** 
 * ClassName:Learnpipe2 <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年7月6日 下午4:44:48 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
public class Learnpipe2 {

	public static void main(String[] args) {
		
		SparkConf conf = new SparkConf().setAppName(Learnpipe2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);

		 List<String> datas = Arrays.asList("hi", "hello", "how", "are", "you");
		 JavaRDD<String> pipeRDD =   sc.parallelize(datas).pipe("scripts/echo.sh");
		 //pipeRDD.collect().forEach(System.out::println);
		 JavaRDD<String> pipemapRDD = pipeRDD.map(new Function<String,String>() {
		    public String call(String v1) throws Exception {
		        return v1+"!";
		    }
		});
		 PrintUtilPro.printList(pipemapRDD.collect());
	}

}
 