package sparklearnscala.sql.source

import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

object ParquetOpts {
  def main(args: Array[String]): Unit = {
    parquetRead;
  }
  
  def parquetRead():Unit = {
		val conf = new SparkConf().setAppName(ParquetOpts.getClass.getSimpleName()).setMaster("local");
		val sc = new SparkContext(conf);
		
		val sqlContext = new SQLContext(sc);

		//DataFrame df = sqlContext.read().parquet("data/user.parquet");
		//DataFrame df = sqlContext.read().format("parquet").load("data/user.parquet");
		
		val df = sqlContext.sql("SELECT * FROM parquet.`data/user.parquet`");

		df.show();
		//df.write().save("data/userparquet");
	}
}