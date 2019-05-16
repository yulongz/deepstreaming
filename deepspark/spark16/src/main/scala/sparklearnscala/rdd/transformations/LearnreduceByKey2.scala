package sparklearnscala.rdd.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object LearnreduceByKey2 {
  def main(args: Array[String]): Unit = {
    
    val conf = new SparkConf().setAppName(LearnreduceByKey2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)

    val textFile = sc.textFile("data/log4j.properties")
    val words = textFile.flatMap(line => line.split(" "))
    val wordPairs = words.map(word => (word, 1))
    val wordCounts = wordPairs.reduceByKey((a, b) => a + b)
    println("wordCounts: ")
    wordCounts.collect().foreach(println)
  }
}