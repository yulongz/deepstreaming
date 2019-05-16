/** 
 * Project Name:sparklearn 
 * File Name:LearnmapPartitionsWithIndex2.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月4日下午5:35:20 
 * sky.zyl@hotmail.com
*/  
  
package sparklearn.rdd.transformations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.VoidFunction;

import sparklearn.util.PrintUtilPro;

/** 
 * ClassName:LearnmapPartitionsWithIndex2 <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年7月4日 下午5:35:20 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
public class LearnmapPartitionsWithIndex2 {

	public static void main(String[] args) {
		 SparkConf conf = new SparkConf().setAppName(LearnmapPartitionsWithIndex2.class.getSimpleName())
		            .setMaster("local");

		    JavaSparkContext sc = new JavaSparkContext(conf);

		    List<String> names = Arrays.asList("张三1", "李四1", "王五1", "张三2", "李四2",
		            "王五2", "张三3", "李四3", "王五3", "张三4");

		    // 初始化，分为3个分区
		    JavaRDD<String> namesRDD = sc.parallelize(names, 3);
		    JavaRDD<String> mapPartitionsWithIndexRDD = namesRDD.mapPartitionsWithIndex(
		            new Function2<Integer, Iterator<String>, Iterator<String>>() {

		                private static final long serialVersionUID = 1L;

		                public Iterator<String> call(Integer v1, Iterator<String> v2) throws Exception {
		                    List<String> list = new ArrayList<String>();
		                    while (v2.hasNext()) {
		                        list.add("分区索引:" + v1 + "\t" + v2.next());
		                    }
		                    return list.iterator();
		                }
		            },
		            true);

		    // 从集群获取数据到本地内存中
		    List<String> result = mapPartitionsWithIndexRDD.collect();
		    
		   //result.forEach(System.out::println);
			PrintUtilPro.printList(result);


		    sc.close();
	}

}
 