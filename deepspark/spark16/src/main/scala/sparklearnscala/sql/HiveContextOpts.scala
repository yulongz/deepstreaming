package sparklearnscala.sql

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

object HiveContextOpts {
  def main(args: Array[String]): Unit = {
    hiveRead;
  }

  def hiveRead {
    val conf = new SparkConf().setAppName(HiveContextOpts.getClass.getSimpleName()).setMaster("local");
    val sc = new SparkContext(conf);
    //val sqlContext = new SQLContext(sc);
    val sqlContext = new org.apache.spark.sql.hive.HiveContext(sc)
    sqlContext.setConf("fs.default.name", "hdfs://breath:9000");
    sqlContext.setConf("hive.metastore.uris", "thrift://breath:9083");
    sqlContext.setConf("javax.jdo.option.ConnectionURL",
      "jdbc:mysql://breath:3306/hive?createDatabaseIfNotExist=true&amp;characterEncoding=UTF-8");
    sqlContext.setConf("javax.jdo.option.ConnectionDriverName", "com.mysql.jdbc.Driver");

    sqlContext.sql(
      "CREATE TABLE IF NOT EXISTS userinfo (id INT, name STRING, age INT) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' STORED AS TEXTFILE");
    sqlContext.sql("LOAD DATA LOCAL INPATH 'data/userinfo.txt' OVERWRITE INTO TABLE userinfo ");

    // Queries are expressed in HiveQL.
    val results = sqlContext.sql("FROM userinfo SELECT name, age").collect();

    results.foreach(println)

    sqlContext.sql("CREATE TABLE IF NOT EXISTS userinfobak AS SELECT name,age FROM userinfo");

  }
}