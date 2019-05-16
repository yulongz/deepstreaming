package sparklearnscala.rdd.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Learnunion2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(Learnunion2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)

    val datas1: Array[String] = Array("张三", "李四")
    val datas2:Array[String] = Array("tom", "gim")
    
     sc.parallelize(datas1)
     .union(sc.parallelize(datas2))
     .foreach(println)

// 或
//(sc.parallelize(datas1) ++ sc.parallelize(datas2)).foreach(println)
  }
}