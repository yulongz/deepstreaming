package sparklearnscala.rdd.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Learncoalesce2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(Learncoalesce2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)
    val datas = List("hi", "hello", "how", "are", "you")
    val datasRDD = sc.parallelize(datas, 4)
    println("RDD的分区数: " + datasRDD.partitions.length)
    val datasRDD2 = datasRDD.coalesce(2,false)
    println("RDD的分区数: " + datasRDD2.partitions.length)
  }
}