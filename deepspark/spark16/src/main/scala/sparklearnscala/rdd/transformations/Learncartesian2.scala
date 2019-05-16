package sparklearnscala.rdd.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Learncartesian2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(Learncartesian2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)

    val namesRDD = sc.parallelize(Array("张三", "李四", "王五"))

    val scoreRDD = sc.parallelize(Array(60, 70, 80))

    namesRDD.cartesian(scoreRDD)
      .foreach(println)
  }
}