package sparklearnscala.rdd.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Learnjoin2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(Learnjoin2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)

    sc.parallelize(List((1, "苹果"), (2, "梨"), (3, "香蕉"), (4, "石榴")))
      .join(sc.parallelize(List((1, 7), (2, 3), (3, 8), (4, 3), (5, 9))))
      .foreach(println)
  }
}