package sparklearnscala.rdd.action

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object LearncountByKey2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(LearncountByKey2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)
    
    println(sc.parallelize(Array(("A", 1), ("B", 6), ("A", 2), ("C", 1), ("A", 7), ("A", 8)))
            .countByKey())
  }
}