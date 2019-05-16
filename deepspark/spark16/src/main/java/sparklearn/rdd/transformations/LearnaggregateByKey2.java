/** 
 * Project Name:sparklearn 
 * File Name:LearnaggregateByKey2.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月6日下午4:21:35 
 * sky.zyl@hotmail.com
*/  
  
package sparklearn.rdd.transformations;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;

import scala.Tuple2;
import sparklearn.util.PrintUtilPro;

/** 
 * ClassName:LearnaggregateByKey2 <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年7月6日 下午4:21:35 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
public class LearnaggregateByKey2 {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName(LearnaggregateByKey2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);
		
		List<Tuple2<Integer, Integer>> datas = new ArrayList<>();
	    datas.add(new Tuple2<>(1, 3));
	    datas.add(new Tuple2<>(1, 2));
	    datas.add(new Tuple2<>(1, 4));
	    datas.add(new Tuple2<>(2, 3));

	    JavaPairRDD<Integer, Integer> strRDD = sc.parallelizePairs(datas, 2)
	            .aggregateByKey(
	                    0,
	                    new Function2<Integer, Integer, Integer>() {
	                        @Override
	                        public Integer call(Integer v1, Integer v2) throws Exception {
	                            System.out.println("seq: " + v1 + "\t" + v2);
	                            return Math.max(v1, v2);
	                        }
	                    },
	                    new Function2<Integer, Integer, Integer>() {
	                        @Override
	                        public Integer call(Integer v1, Integer v2) throws Exception {
	                            System.out.println("comb: " + v1 + "\t" + v2);
	                            return v1 + v2;
	                        }
	                    });
	    //strRDD.collect().forEach(System.out::println);
	    PrintUtilPro.printList(strRDD.collect());
		
		

	}

}
 