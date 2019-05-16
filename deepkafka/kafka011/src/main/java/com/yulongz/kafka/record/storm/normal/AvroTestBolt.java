package com.yulongz.kafka.record.storm.normal;

import org.apache.avro.generic.GenericRecord;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

/**
 * Created by kafka on 17-12-20.
 */
public class AvroTestBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        System.out.println("-------------");

        GenericRecord value = (GenericRecord) tuple.getValueByField("value");
        System.out.println(value.toString());
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
