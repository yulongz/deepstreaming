package sparklearnscala.other

import org.apache.spark.{ SparkContext, SparkConf }
import org.apache.spark.SparkContext._
import org.apache.hadoop.io.compress.GzipCodec
object Spark_Template {
  def main(args: Array[String]) {
    var conf = new SparkConf().setAppName("SparkTemplate").setMaster("local")
    var sc = new SparkContext(conf)
    val datafile = sc.textFile("file:///home/hadoop/123txt",10)
    datafile.saveAsTextFile("file:///home/hadoop/123gz", classOf[GzipCodec])
    sc.stop()
  }
}