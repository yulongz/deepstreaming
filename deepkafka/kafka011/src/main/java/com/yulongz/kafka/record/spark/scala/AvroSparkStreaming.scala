package com.yulongz.kafka.record.spark.scala

import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, StreamingContext}
import utils.PropsUtil

object AvroSparkStreaming {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName("spark")
    val ssc = new StreamingContext(conf, Seconds(1))

    val sourceTopic: String = PropsUtil.loadProps("my.properties").getProperty("sparksourcetopic")
    val groupId: String = PropsUtil.loadProps("my.properties").getProperty("spark.group.id")
    val bootstrapServers: String = PropsUtil.loadProps("my.properties").getProperty("bootstrap.servers")
    val url: String = PropsUtil.loadProps("my.properties").getProperty("kafka.schema.registry.url")


    val kafkaParams = Map[String, AnyRef](
      "bootstrap.servers" -> bootstrapServers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[KafkaAvroDeserializer],
      "group.id" -> groupId,
      "auto.offset.reset" -> "latest",
      "schema.registry.url"-> url
    )

    val topics = Set(sourceTopic)
    val directStream: InputDStream[ConsumerRecord[AnyRef, AnyRef]] = KafkaUtils.createDirectStream(ssc,
      PreferConsistent,
      Subscribe(topics, kafkaParams));
    directStream.map{record => (record.value.asInstanceOf[GenericRecord].toString)}.print
    ssc.start()
    ssc.awaitTermination()
  }
}