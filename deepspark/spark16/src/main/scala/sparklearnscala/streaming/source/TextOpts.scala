package sparklearnscala.streaming.source

import org.apache.spark.SparkConf
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds

object TextOpts {
  
  def main(args: Array[String]): Unit = {
    textOpts;
  }
  def textOpts = {
    // Create a local StreamingContext with two working thread and batch interval of 1 second.
    // The master requires 2 cores to prevent from a starvation scenario.
    val conf = new SparkConf().setMaster("local[1]").setAppName("TextOpts")
    val ssc = new StreamingContext(conf, Seconds(5))

    // Create a DStream that will connect to hostname:port, like localhost:9999
    val lines = ssc.textFileStream("data/textfilestream")

    // Split each line into words
    val words = lines.flatMap(_.split(" "))

    // Count each word in each batch
    val pairs = words.map(word => (word, 1))
    val wordCounts = pairs.reduceByKey(_ + _)

    // Print the first ten elements of each RDD generated in this DStream to the console
    wordCounts.print()

    ssc.start() // Start the computation
    ssc.awaitTermination() // Wait for the computation to terminate
  }
  
}