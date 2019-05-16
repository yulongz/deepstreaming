/** 
 * Project Name:sparklearn 
 * File Name:JsonOpts.java 
 * Package Name:sparklearn.sql 
 * Date:2017年7月12日下午4:11:59 
 * sky.zyl@hotmail.com
*/

package sparklearn.sql.source;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SaveMode;

/**
 * ClassName:JsonOpts <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月12日 下午4:11:59 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class JsonOpts {
	public static void main(String[] args) {
		//jsonRead();
		jsonParallelize();
	}

	public static void jsonRead() {
		SparkConf conf = new SparkConf().setAppName(JsonOpts.class.getSimpleName()).setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		SQLContext sqlContext = new org.apache.spark.sql.SQLContext(sc);

		//DataFrame df = sqlContext.read().json("data/user.json");
		DataFrame df = sqlContext.read().format("json").load("data/user.json");
		df.show();
		//df.write().save("data/userparquet");
		//df.write().mode(SaveMode.Overwrite).save("data/userparquet");
	}
	
	public static void jsonParallelize(){
		SparkConf conf = new SparkConf().setAppName(JsonOpts.class.getSimpleName()).setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		SQLContext sqlContext = new org.apache.spark.sql.SQLContext(sc);
		// Alternatively, a DataFrame can be created for a JSON dataset represented by
		// an RDD[String] storing one JSON object per string.
		List<String> jsonData = Arrays.asList(
		  "{\"name\":\"Yin\",\"address\":{\"city\":\"Columbus\",\"state\":\"Ohio\"}}");
		JavaRDD<String> anotherPeopleRDD = sc.parallelize(jsonData);
		DataFrame anotherPeople = sqlContext.read().json(anotherPeopleRDD);
		anotherPeople.show();
	}

}
