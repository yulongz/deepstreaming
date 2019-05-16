package sparklearnscala.rdd.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object LearnaggregateByKey2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(LearnaggregateByKey2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)

    // 合并在同一个partition中的值，a的数据类型为zeroValue的数据类型，b的数据类型为原value的数据类型
    def seq(a:Int, b:Int): Int = {
        println("seq: " + a + "\t" + b)
        math.max(a, b)
    }

    // 合并在不同partition中的值，a,b的数据类型为zeroValue的数据类型
    def comb(a:Int, b:Int): Int = {
        println("comb: " + a + "\t" + b)
        a + b
    }

    //先在各自分区里内对value进行seq函数计算(zeroValue主要是为了在seq函数中作为第一个默认值来进行的，有一定的作用)，得到中间结果k v；然后把所有分区的value进行com函数操作
    
    // 数据拆分成两个分区
    // 分区一数据: (1,3) (1,2)
    // 分区二数据: (1,4) (2,3)
    // zeroValue 中位值，定义返回value的类型，并参与运算
    // seqOp 用来在一个partition中合并值的
    // 分区一相同key的数据进行合并
    // seq: 0   3  (1,3)开始和中位值合并为3
    // seq: 3   2  (1,2)再次合并为3
    // 分区二相同key的数据进行合并
    // seq: 0   4  (1,4)开始和中位值合并为4
    // seq: 0   3  (2,3)开始和中位值合并为3
    // comb 用来在不同partition中合并值的
    // 将两个分区的结果进行合并
    // key为1的, 两个分区都有, 合并为(1,7)
    // key为2的, 只有一个分区有, 不需要合并(2,3)
    sc.parallelize(List((1, 3), (1, 2), (1, 4), (2, 3)), 2)
        .aggregateByKey(0)(seq, comb)
        .collect()
        .foreach(println)
  }
}