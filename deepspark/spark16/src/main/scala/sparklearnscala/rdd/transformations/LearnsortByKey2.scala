package sparklearnscala.rdd.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object LearnsortByKey2 {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(LearnsortByKey2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)

    sc.parallelize(Array(60, 70, 80, 55, 45, 75))
      .sortBy(v => v, false)
      .foreach(println)

    sc.parallelize(List((3, 3), (2, 2), (1, 4), (2, 3)))
      .sortByKey(true)
      .foreach(println)
  }

}