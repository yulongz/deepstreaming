package sparklearnscala.streaming

import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.HashPartitioner

object StatefulNetworkWordCount {
  def main(args: Array[String]) {

    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

    val updateFunc = (values: Seq[Int], state: Option[Int]) => {
      val currentCount = values.sum

      val previousCount = state.getOrElse(0)

      Some(currentCount + previousCount)
    }

    val newUpdateFunc = (iterator: Iterator[(String, Seq[Int], Option[Int])]) => {
      iterator.flatMap(t => updateFunc(t._2, t._3).map(s => (t._1, s)))
    }

    val sparkConf = new SparkConf().setAppName("StatefulNetworkWordCount").setMaster("local")
    // Create the context with a 5 second batch size
    val ssc = new StreamingContext(sparkConf, Seconds(5))
    ssc.checkpoint("data/statefulnetworkwordcount")

    // Initial RDD input to updateStateByKey
    val initialRDD = ssc.sparkContext.parallelize(List(("hello", 1), ("world", 1)))

    // Create a ReceiverInputDStream on target ip:port and count the
    // words in input stream of \n delimited test (eg. generated by 'nc')
    val lines = ssc.socketTextStream("localhost", 9999)
    val words = lines.flatMap(_.split(" "))
    val wordDstream = words.map(x => (x, 1))

    // Update the cumulative count using updateStateByKey
    // This will give a Dstream made of state (which is the cumulative count of the words)
    val stateDstream = wordDstream.updateStateByKey[Int](newUpdateFunc,
      new HashPartitioner(ssc.sparkContext.defaultParallelism), true, initialRDD)
    stateDstream.print()
    ssc.start()
    ssc.awaitTermination()
  }
}