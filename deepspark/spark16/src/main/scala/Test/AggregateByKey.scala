package Test

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by Edward on 2016/10/27.
  */
object AggregateByKey {
  def main(args: Array[String]) {
    val sparkConf: SparkConf = new SparkConf().setAppName("AggregateByKey")
      .setMaster("local")
    val sc: SparkContext = new SparkContext(sparkConf)

    val data = List((1, 3), (1, 2), (1, 4), (2, 3))
    var rdd = sc.parallelize(data,2)//数据拆分成两个分区

    //合并在不同partition中的值，a,b的数据类型为zeroValue的数据类型
    def comb(a: String, b: String): String = {
      println("comb: " + a + "\t " + b)
      a + b
    }
    //合并在同一个partition中的值， a的数据类型为zeroValue的数据类型，b的数据类型为原value的数据类型
    def seq(a: String, b: Int): String = {
      println("seq: " + a + "\t " + b)
      a + b
    }

    rdd.foreach(println)
    
    //zeroValue 中立值，定义返回value的类型，并参与运算
    //seqOp 用来在一个partition中合并值的
    //comb 用来在不同partition中合并值的
    val aggregateByKeyRDD: RDD[(Int, String)] = rdd.aggregateByKey("100")(seq,comb)

    //打印输出
    aggregateByKeyRDD.foreach(println)

    sc.stop()
  }
}