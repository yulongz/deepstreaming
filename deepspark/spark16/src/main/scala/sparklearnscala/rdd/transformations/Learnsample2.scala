package sparklearnscala.rdd.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Learnsample2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(Learnsample2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)

    val datas: Array[Int] = Array(1, 2, 3, 7, 4, 5, 8)

    sc.parallelize(datas)
      .sample(withReplacement = false, 0.5, System.currentTimeMillis)
      .foreach(println)

  }
}