/** 
 * Project Name:sparklearn 
 * File Name:HiveContextOpts.java 
 * Package Name:sparklearn.sql 
 * Date:2017年7月12日下午5:04:20 
 * sky.zyl@hotmail.com
*/

package sparklearn.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.hive.HiveContext;

/**
 * ClassName:HiveContextOpts <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月12日 下午5:04:20 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class HiveContextOpts {

	public static void main(String[] args) {
		hiveRead();
	}

	public static void hiveRead() {
		SparkConf conf = new SparkConf().setAppName(HiveContextOpts.class.getSimpleName()).setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);

		// sc is an existing JavaSparkContext.
		HiveContext sqlContext = new org.apache.spark.sql.hive.HiveContext(sc.sc());
		sqlContext.setConf("fs.default.name", "hdfs://breath:9000");
		sqlContext.setConf("hive.metastore.uris", "thrift://breath:9083");
		sqlContext.setConf("javax.jdo.option.ConnectionURL",
				"jdbc:mysql://breath:3306/hive?createDatabaseIfNotExist=true&amp;characterEncoding=UTF-8");
		sqlContext.setConf("javax.jdo.option.ConnectionDriverName", "com.mysql.jdbc.Driver");

		sqlContext.sql(
				"CREATE TABLE IF NOT EXISTS userinfo (id INT, name STRING, age INT) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' STORED AS TEXTFILE");
		sqlContext.sql("LOAD DATA LOCAL INPATH 'data/userinfo.txt' OVERWRITE INTO TABLE userinfo ");

		// Queries are expressed in HiveQL.
		Row[] results = sqlContext.sql("FROM userinfo SELECT name, age").collect();

		for (Row row : results) {
			System.out.println(row.getString(0) + ":" + row.getInt(1));
		}
		sqlContext.sql("CREATE TABLE IF NOT EXISTS userinfobak AS SELECT name,age FROM userinfo");
	}
}
