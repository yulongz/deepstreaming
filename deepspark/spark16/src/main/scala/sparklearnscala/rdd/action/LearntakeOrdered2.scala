package sparklearnscala.rdd.action

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object LearntakeOrdered2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(LearntakeOrdered2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)

    sc.parallelize(Array(5, 6, 2, 1, 7, 8))
      .takeOrdered(3)(new Ordering[Int]() {
        override def compare(x: Int, y: Int): Int = y.compareTo(x)
      })
      .foreach(println)
  }
}