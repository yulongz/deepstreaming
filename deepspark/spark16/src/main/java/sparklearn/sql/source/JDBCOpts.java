/** 
 * Project Name:sparklearn 
 * File Name:JDBCOpts.java 
 * Package Name:sparklearn.sql.source 
 * Date:2017年7月12日下午5:46:11 
 * sky.zyl@hotmail.com
*/  
  
package sparklearn.sql.source;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

import sparklearn.sql.rddtodf.SchemaUsingReflection;

/** 
 * ClassName:JDBCOpts <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年7月12日 下午5:46:11 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
public class JDBCOpts {

	public static void main(String[] args) {
		
		readJDBC();

	}
	public static void readJDBC(){
		// sc is an existing JavaSparkContext.
		SparkConf conf = new SparkConf().setAppName(SchemaUsingReflection.class.getSimpleName()).setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		SQLContext sqlContext = new org.apache.spark.sql.SQLContext(sc);
		
		Properties prop=new Properties();
		prop.put("user","tarena");
		prop.put("password","tarena123");
		//oracle.jdbc.driver.OracleDriver
		
		DataFrame jdbcDF = sqlContext.read().jdbc("jdbc:oracle:thin:@172.16.13.80:1521:orcl","tarena.test",prop);
		jdbcDF.show();
	}

}
 