/** 
 * Project Name:sparklearn 
 * File Name:DataFrameOpts.java 
 * Package Name:sparklearn.sql 
 * Date:2017年7月12日下午4:16:18 
 * sky.zyl@hotmail.com
*/  
  
package sparklearn.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import sparklearn.util.PrintUtilPro;

/** 
 * ClassName:DataFrameOpts <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年7月12日 下午4:16:18 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
public class DataFrameOpts {

	public static void main(String[] args) {
		dfOpts();
	}
	public static void dfOpts(){
		SparkConf conf = new SparkConf().setAppName(DataFrameOpts.class.getSimpleName()).setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		SQLContext sqlContext = new org.apache.spark.sql.SQLContext(sc);

		DataFrame df = sqlContext.read().json("data/user.json");
		df.show();
		

		// Show the content of the DataFrame
		df.show();
		// age name
		// null Michael
		// 30 Andy
		// 19 Justin

		// Print the schema in a tree format
		df.printSchema();
		// root
		// |-- age: long (nullable = true)
		// |-- name: string (nullable = true)

		// Select only the "name" column
		df.select("name").show();
		// name
		// Michael
		// Andy
		// Justin

		// Select everybody, but increment the age by 1
		df.select(df.col("name"), df.col("age").plus(1)).show();
		// name (age + 1)
		// Michael null
		// Andy 31
		// Justin 20

		// Select people older than 21
		df.filter(df.col("age").gt(21)).show();
		// age name
		// 30 Andy

		// Count people by age
		df.groupBy("age").count().show();
		// age count
		// null 1
		// 19 1
		// 30 1
		
		JavaRDD<String> dfRDD = df.javaRDD().map((new Function<Row, String>() {
			  public String call(Row row) {
				    return "Name: " + row.getString(1);
				  }
				}));
		PrintUtilPro.printList(dfRDD.collect());
		
	}
}
 