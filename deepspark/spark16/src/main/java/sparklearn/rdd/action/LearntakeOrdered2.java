/** 
 * Project Name:sparklearn 
 * File Name:LearntakeOrdered2.java 
 * Package Name:sparklearn.rdd.action 
 * Date:2017年7月6日下午5:27:39 
 * sky.zyl@hotmail.com
*/  
  
package sparklearn.rdd.action;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import sparklearn.util.PrintUtilPro;

/** 
 * ClassName:LearntakeOrdered2 <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年7月6日 下午5:27:39 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
public class LearntakeOrdered2 {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName(LearntakeOrdered2.class.getSimpleName()).setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);

		List<Integer> datas = Arrays.asList(5, 6, 2, 1, 7, 8);

		JavaRDD<Integer> dataRDD = sc.parallelize(datas);
		
		Comparator<Integer> mycomp= new MyComp(true);
		
		PrintUtilPro.printList(dataRDD.takeOrdered(3));
		//PrintUtilPro.printList(dataRDD.takeOrdered(3,mycomp));//exist problem

		sc.close();
		
		//System.out.println(new MyComp(true).compare(5, 4));
	}
}

class MyComp implements Comparator<Integer>,Serializable{
	/** 
	 * serialVersionUID:TODO
	 */  
	private static final long serialVersionUID = 1L;
	boolean bo ;
    MyComp(boolean bo){
    	this.bo = bo;
    }
	public int compare(Integer o1, Integer o2) {
		if(bo){//true  smaller
			return (o1>=o2)?o2:o1;	
		}else{
			return (o1>=o2)?o1:o2;////false  smaller
		}
	}
}
 