package sparklearnscala.streaming.zkoffsets

import kafka.serializer.StringDecoder
import org.apache.spark.streaming.kafka.KafkaUtils
import com.typesafe.config.ConfigFactory
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.SparkConf
import org.apache.log4j.Logger;
import scala.collection.JavaConverters._ //隐式转换，这样才能有asScala这个方法
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.Time

object DirectKafkaDefaultExample {
  private val conf = ConfigFactory.load()
  private val sparkStreamingConf = conf.getStringList("DirectKafkaDefaultExample-List").asScala
  val logger = Logger.getLogger(DirectKafkaDefaultExample.getClass)
  def main(args: Array[String]) {
    //    if (args.length < 2) {
    //      System.exit(1)
    //    }
    //val Array(brokers, topics) = args
    val Array(brokers, topics) = Array("localhost:9092", "test")
    val checkpointDir = "data/kafkaoffsets/checkpointLogs"
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers,"auto.offset.reset" -> "smallest")//largest 
    // Extract : Create direct kafka stream with brokers and topics
    val topicsSet = topics.split(",").toSet

    //    val kafkaParams = Map[String, String](
    //      "bootstrap.servers" -> "sparktest:9092",
    //      "group.id" -> "group1")
    //    val topics = Set("test")

    val ssc = StreamingContext.getOrCreate(checkpointDir, setupSsc(topicsSet, kafkaParams, checkpointDir) _)
    ssc.start() // Start the spark streaming
    ssc.awaitTermination();
  }
  def setupSsc(topicsSet: Set[String], kafkaParams: Map[String, String], checkpointDir: String)(): StreamingContext =
    { //setting sparkConf with configurations
      val sparkConf = new SparkConf()
      sparkConf.setAppName("DirectKafkaDefaultExample")
      sparkConf.setMaster("local")
      sparkStreamingConf.foreach { x => val split = x.split("="); sparkConf.set(split(0), split(1)); }
      val ssc = new StreamingContext(sparkConf, Seconds(5))
      val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
        ssc, kafkaParams, topicsSet)
      val line = messages.map(_._2)
      val lines = line.flatMap(line => line.split("\n"))
      //val filteredLines = lines.filter { x => LogFilter.filter(x, "1") }
      val filteredLines = lines

      //filteredLines.flatMap(line=>line.split(" ")).map(x => (x, 1)).reduceByKey(_ + _).print()
      filteredLines.foreachRDD((rdd: RDD[String], time: Time) => {
        rdd.foreachPartition { partitionOfRecords =>
          {
            if (partitionOfRecords.isEmpty) {
              //logger.warn("partitionOfRecords FOUND EMPTY ,IGNORING THIS PARTITION")
            } else {
              //logger.warn("partitionOfRecords FOUND NOT EMPTY ,COMPUTING THIS PARTITION")
              /* write computation logic here  */
              partitionOfRecords.flatMap(_.split(" ")).map(word => (word, 1)).foreach(println)

            }
          } //partition ends
        } //foreachRDD ends
      })
      ssc.checkpoint(checkpointDir) // the offset ranges for the stream will be stored in the checkpoint
      ssc
    }

}