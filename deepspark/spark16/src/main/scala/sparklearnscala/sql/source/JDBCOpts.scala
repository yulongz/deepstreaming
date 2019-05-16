package sparklearnscala.sql.source

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import java.util.Properties

object JDBCOpts {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(JDBCOpts.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)
    val sqlc = new SQLContext(sc)

    val prop = new Properties();
    prop.put("user", "tarena");
    prop.put("password", "tarena123");
    //oracle.jdbc.driver.OracleDriver
    val jdbcDF = sqlc.read.jdbc("jdbc:oracle:thin:@172.16.13.80:1521:orcl", "tarena.test", prop)
    jdbcDF.show()
  }
}