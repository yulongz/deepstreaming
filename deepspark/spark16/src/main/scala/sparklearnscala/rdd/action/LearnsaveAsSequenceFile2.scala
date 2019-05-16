package sparklearnscala.rdd.action

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import scala.sys.process._


object LearnsaveAsSequenceFile2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(LearnsaveAsSequenceFile2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)
    
    
    val cmd = Seq("rm", "-rf", "data/saveAsSequenceFile")
    cmd.lines
    
    sc.parallelize(Array(5, 6, 2, 1, 7, 8))
      .saveAsTextFile("data/saveAsSequenceFile")
  }
}