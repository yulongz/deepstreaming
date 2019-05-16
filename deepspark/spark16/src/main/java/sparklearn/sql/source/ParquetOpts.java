/** 
 * Project Name:sparklearn 
 * File Name:ParquetOpts.java 
 * Package Name:sparklearn.sql 
 * Date:2017年7月12日下午4:27:50 
 * sky.zyl@hotmail.com
*/  
  
package sparklearn.sql.source;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

/** 
 * ClassName:ParquetOpts <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年7月12日 下午4:27:50 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
public class ParquetOpts {

	public static void main(String[] args) {
		parquetRead();

	}
	
	public static void parquetRead() {
		SparkConf conf = new SparkConf().setAppName(JsonOpts.class.getSimpleName()).setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		SQLContext sqlContext = new org.apache.spark.sql.SQLContext(sc);

		//DataFrame df = sqlContext.read().parquet("data/user.parquet");
		//DataFrame df = sqlContext.read().format("parquet").load("data/user.parquet");
		
		DataFrame df = sqlContext.sql("SELECT * FROM parquet.`data/user.parquet`");

		df.show();
		//df.write().save("data/userparquet");
	}

}
 