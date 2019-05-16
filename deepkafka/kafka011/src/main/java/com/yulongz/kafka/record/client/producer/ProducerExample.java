/**
 * Copyright 2015 Confluent Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yulongz.kafka.record.client.producer;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import utils.PropsUtil;

import java.util.Properties;
import java.util.Random;

public class ProducerExample {
  public static void main(String[] args){
    long events = 10;

    // http://localhost:8081
    String url = PropsUtil.loadProps("my.properties").getProperty("kafka.schema.registry.url");

    Properties props = new Properties();
    props.put("bootstrap.servers", PropsUtil.loadProps("my.properties").getProperty("bootstrap.servers"));
    props.put("acks", "all");
    props.put("retries", 0);
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
    props.put("schema.registry.url", url);

    String schemaString = "{\"namespace\": \"com.yulongz.example.avro\", \"type\": \"record\", " +
                           "\"name\": \"page_visit\"," +
                           "\"fields\": [" +
                            "{\"name\": \"time\", \"type\": \"long\"}," +
                            "{\"name\": \"site\", \"type\": \"string\"}," +
                            "{\"name\": \"ip\", \"type\": \"string\"}" +
                           "]}";
    Producer<String, GenericRecord> producer = new KafkaProducer<String, GenericRecord>(props);

    Schema.Parser parser = new Schema.Parser();
    Schema schema = parser.parse(schemaString);

    Random rnd = new Random();
    for (long nEvents = 0; nEvents < events; nEvents++) {
      long runtime = System.currentTimeMillis();
      String site = "www.avroexample.example.com";
      String ip = "192.168.2." + rnd.nextInt(255);

      GenericRecord page_visit = new GenericData.Record(schema);
      page_visit.put("time", runtime);
      page_visit.put("site", site);
      page_visit.put("ip", ip);

      String producerTopic = PropsUtil.loadProps("my.properties").getProperty("produceravrotopic");

      ProducerRecord<String, GenericRecord> data = new ProducerRecord<String, GenericRecord>(
              producerTopic, ip, page_visit);
      producer.send(data);
    }

    producer.close();
  }
}
