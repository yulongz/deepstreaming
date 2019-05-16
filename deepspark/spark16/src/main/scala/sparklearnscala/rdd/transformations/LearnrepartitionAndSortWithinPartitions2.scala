package sparklearnscala.rdd.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import scala.util.Random
import org.apache.spark.Partitioner

object LearnrepartitionAndSortWithinPartitions2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(LearnrepartitionAndSortWithinPartitions2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)
    def partitionFunc(key:String): Int = {
        key.substring(7).toInt
    }

    val datas = new Array[String](100)
    val random = new Random(1)
    for (i <- 0 until 10; j <- 0 until 10) {
        val index: Int = i * 10 + j
        datas(index) = "product" + random.nextInt(10) + ",url" + random.nextInt(10)
    }
    val datasRDD = sc.parallelize(datas)
    val pairRDD = datasRDD.map(line => (line, 1))
        .reduceByKey((a, b) => a + b)
       
    pairRDD.foreach(println)

    println("===================================")
    
    pairRDD.repartitionAndSortWithinPartitions(new Partitioner() {
        override def numPartitions: Int = 10

        override def getPartition(key: Any): Int = {
            val str = String.valueOf(key)
            str.substring(7, str.indexOf(',')).toInt
        }
    }).foreach(println)
  }
}