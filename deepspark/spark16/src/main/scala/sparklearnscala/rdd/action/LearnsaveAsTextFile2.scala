package sparklearnscala.rdd.action

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import scala.sys.process._

object LearnsaveAsTextFile2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(LearnsaveAsTextFile2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)
    
    val cmd = Seq("rm", "-rf", "data/saveAsTextFile")
    cmd.lines
    
    sc.parallelize(Array(5,6,2,1,7,8))
            .saveAsTextFile("data/saveAsTextFile")
  }
}