package GraphX

import org.apache.spark._
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql._

object Test {
  def main(args: Array[String]) {
    //屏蔽日志
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
    
    val conf = new SparkConf().setAppName("textFile").setMaster("local")
    val sc = new SparkContext(conf) // An existing SparkContext.
    val data = Array(1, 2, 3, 4, 5)
    val distData = sc.parallelize(data)
    distData.foreach(println)
    
    sc.stop()
  }
}