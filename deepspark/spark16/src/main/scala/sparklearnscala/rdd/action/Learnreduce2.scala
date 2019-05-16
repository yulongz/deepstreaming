package sparklearnscala.rdd.action

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Learnreduce2 {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName(Learnreduce2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)

    println(sc.parallelize(1 to 10)
      .reduce((x, y) => x + y))
  }
}