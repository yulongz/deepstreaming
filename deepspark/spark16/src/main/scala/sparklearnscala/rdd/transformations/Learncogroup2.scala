package sparklearnscala.rdd.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Learncogroup2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(Learncogroup2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)
    val datas1 = List((1, "苹果"),
      (2, "梨"),
      (3, "香蕉"),
      (4, "石榴"))

    val datas2 = List((1, 7),
      (2, 3),
      (3, 8),
      (4, 3))

    val datas3 = List((1, "7"),
      (2, "3"),
      (3, "8"),
      (4, "3"),
      (4, "4"),
      (4, "5"),
      (4, "6"))

    sc.parallelize(datas1)
      .cogroup(sc.parallelize(datas2),
        sc.parallelize(datas3))
      .foreach(println)
  }
}