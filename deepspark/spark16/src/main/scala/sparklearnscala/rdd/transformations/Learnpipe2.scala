package sparklearnscala.rdd.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Learnpipe2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(Learnpipe2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)
    val data = List("hi", "hello", "how", "are", "you")
    val pipedata = sc.makeRDD(data)
      .pipe("scripts/echo.sh")
      
    val list = pipedata.collect()
    
    list.map { x => x+"!" }.foreach(println)
   
  }
}