package sparklearnscala.rdd.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Learnrepartition2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(Learnrepartition2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)
    val datas = List("hi", "hello", "how", "are", "you")
    val datasRDD = sc.parallelize(datas, 4)
    println("RDD的分区数: " + datasRDD.partitions.length)
    val datasRDD2 = datasRDD.repartition(2) //repartition(num) = coalesce(2,true)
    println("RDD的分区数: " + datasRDD2.partitions.length)
  }
}