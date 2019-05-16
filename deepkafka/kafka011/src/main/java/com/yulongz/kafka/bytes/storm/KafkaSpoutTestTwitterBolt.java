package com.yulongz.kafka.bytes.storm;

import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kafka on 17-12-20.
 */
public class KafkaSpoutTestTwitterBolt extends BaseBasicBolt{
    protected static final Logger logger = LoggerFactory.getLogger(KafkaSpoutTestTwitterBolt.class);
    public static final String USER_SCHEMA = "{\"namespace\": \"com.yulongz.example.avro\", \"type\": \"record\", " +
            "\"name\": \"page_visit\"," +
            "\"fields\": [" +
            "{\"name\": \"time\", \"type\": \"long\"}," +
            "{\"name\": \"site\", \"type\": \"string\"}," +
            "{\"name\": \"ip\", \"type\": \"string\"}" +
            "]}";

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        byte[] value = (byte[]) tuple.getValueByField("value");

        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(USER_SCHEMA);
        Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(schema);
        GenericRecord genericRecord = recordInjection.invert(value).get();
        System.out.println("time= " + genericRecord.get("time")
                + ", site= " + genericRecord.get("site")
                + ", ip=" + genericRecord.get("ip"));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
