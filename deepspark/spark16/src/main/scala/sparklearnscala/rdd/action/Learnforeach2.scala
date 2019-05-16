package sparklearnscala.rdd.action

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Learnforeach2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(Learnforeach2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)
    
    sc.parallelize(1 to 10).collect().foreach(println)
  }
}