package sparklearnscala.rdd.transformations

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object LearnflatMap2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(LearnflatMap2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)

    val datas = Array("aa,bb,cc", "cxf,spring,struts2", "java,C++,javaScript")
    
    sc.parallelize(datas)
            .flatMap(line => line.split(","))
            .foreach(println)
  }
}