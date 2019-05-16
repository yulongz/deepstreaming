package flinklearnscala.batch

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala._

//import org.apache.flink.api.scala.ExecutionEnvironment

object Test {
  def main(args: Array[String]) {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val data = List("hello", "flink cluster", "hello")
    val dataSet = env.fromCollection(data)
    // sum
    val sum = dataSet.flatMap(value => value.split("\\s+")).map(value => (value, 1)).groupBy(0).sum(1)
    sum.print()

    //reduce
    val reduceSet = dataSet.flatMap(value => value.split("\\s+")).map(value => (value, 1)).groupBy(_._1).reduce { (a, b) => (a._1, a._2 + b._2) }
    reduceSet.print()

  }
}