package sparklearnscala.rdd.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Learnfilter2 {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName(Learnfilter2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)

    val datas = Array(1, 2, 3, 7, 4, 5, 8)

    sc.parallelize(datas)
      .filter(v => v >= 3)
      .foreach(println)
  }
}