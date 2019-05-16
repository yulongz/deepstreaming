package sparklearnscala.rdd.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object LearngroupByKey2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(LearngroupByKey2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)

    sc.parallelize(1 to 9, 3)
      .groupBy(x => {
        if (x % 2 == 0) "偶数"
        else "奇数"
      })
      .collect()
      .foreach(println)

    val datas2 = Array("dog", "tiger", "lion", "cat", "spider", "eagle")
    sc.parallelize(datas2)
      .keyBy(_.length)
      .groupByKey()
      .collect()
      .foreach(println)
  }
}