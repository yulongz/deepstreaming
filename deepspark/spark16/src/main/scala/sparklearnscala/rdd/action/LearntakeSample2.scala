package sparklearnscala.rdd.action

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object LearntakeSample2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(LearntakeSample2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)
    
     sc.parallelize(1 to 10)
        .takeSample(withReplacement = false, 3, 1)
        .foreach(println)
    
  }
}