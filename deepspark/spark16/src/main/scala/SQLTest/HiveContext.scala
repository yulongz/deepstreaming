package SQLTest

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

import org.apache.spark.sql.hive._

object HiveContext {
  def main(args: Array[String]) {
    val conf = new SparkConf().
      setAppName("Simple Application").
      setMaster("local")
  }
}