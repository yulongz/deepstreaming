package sparklearnscala.rdd.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Learndistinct2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(Learndistinct2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)

    val datas: Array[String] = Array("张三", "李四", "tom", "张三")

    sc.parallelize(datas).distinct().foreach(println)

  }
}