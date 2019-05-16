package sparklearnscala.rdd.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import scala.collection.mutable.ArrayBuffer

object LearnmapPartitionsWithIndex2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(LearnmapPartitionsWithIndex2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)

    val datas: Array[String] = Array("张三1", "李四1", "王五1", "张三2", "李四2",
      "王五2", "张三3", "李四3", "王五3", "张三4")

    sc.parallelize(datas, 3)
      .mapPartitionsWithIndex(
        (m, n) => {
          val result = ArrayBuffer[String]()
          while (n.hasNext) {
            result.append("分区索引:" + m + "\t" + n.next())
          }
          result.iterator
        })
      .foreach(println)
  }
}