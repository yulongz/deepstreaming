package sparklearnscala.rdd.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Learnintersection2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(Learnintersection2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)

    val datas1: Array[String] = Array("张三", "李四", "tom")
    val datas2:Array[String] = Array("tom", "gim")
    
     sc.parallelize(datas1)
     .intersection(sc.parallelize(datas2))
     .foreach(println)
  }
}