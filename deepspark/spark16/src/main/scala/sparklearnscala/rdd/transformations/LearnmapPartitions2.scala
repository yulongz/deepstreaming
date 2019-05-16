package sparklearnscala.rdd.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import scala.collection.mutable.ArrayBuffer

object LearnmapPartitions {
  def main(args: Array[String]): Unit = {
    
    val conf = new SparkConf().setAppName(LearnmapPartitions.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)

    val datas: Array[String] = Array("张三1", "李四1", "王五1", "张三2", "李四2",
	            "王五2", "张三3", "李四3", "王五3", "张三4")
    
    sc.parallelize(datas, 3)
        .mapPartitions(
            n => {
                val result = ArrayBuffer[String]()
                while (n.hasNext) {
                    result.append(n.next())
                }
                result.iterator
            }
        )
        .foreach(println)
  }
}