/** 
 * Project Name:sparklearn 
 * File Name:Learnmap2.java 
 * Package Name:sparklearn.rdd.transformations 
 * Date:2017年7月4日下午4:40:38 
 * sky.zyl@hotmail.com
*/  
  
package sparklearn.rdd.transformations;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;

import com.google.gson.Gson;

/** 
 * ClassName:Learnmap2 <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年7月4日 下午4:40:38 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
/**
 * map算子
 * <p>
 * map和foreach算子:
 *   1. 循环map调用元的每一个元素;
 *   2. 执行call函数, 并返回.
 * </p>
 */

public class Learnmap2 {
	
	public static void main(String[] args) {

	    SparkConf conf = new SparkConf().setAppName(Learnmap2.class.getSimpleName())
	            .setMaster("local");

	    JavaSparkContext sc = new JavaSparkContext(conf);

	    List<String> datas = Arrays.asList(
	            "{'id':1,'name':'xl1','pwd':'xl123','sex':2}",
	            "{'id':2,'name':'xl2','pwd':'xl123','sex':1}",
	            "{'id':3,'name':'xl3','pwd':'xl123','sex':2}");

	    JavaRDD<String> datasRDD = sc.parallelize(datas);

	    JavaRDD<User> mapRDD = datasRDD.map(
	            new Function<String, User>() {
	                public User call(String v) throws Exception {
	                    Gson gson = new Gson();
	                    return gson.fromJson(v, User.class);
	                }
	            });

	    mapRDD.foreach(new VoidFunction<User>() {
	        public void call(User user) throws Exception {
	            System.out.println("id: " + user.id
	                    + " name: " + user.name
	                    + " pwd: " + user.pwd
	                    + " sex:" + user.sex);
	        }
	    });
	    
	    sc.close();
	}
	
	class User {
		String id;
		String name;
		String pwd;
		String sex;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPwd() {
			return pwd;
		}
		public void setPwd(String pwd) {
			this.pwd = pwd;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}

	}
	
}

 