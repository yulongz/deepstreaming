package com.yulongz.kafka.bytes.storm;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by kafka on 17-12-20.
 */
public class KafkaSpoutTestBolt extends BaseBasicBolt {
    protected static final Logger LOG = LoggerFactory.getLogger(KafkaSpoutTestBolt.class);

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
        GenericDatumReader genericDatumReader = new GenericDatumReader(schema);
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(value);
            BinaryDecoder binaryDecoder = DecoderFactory.get().binaryDecoder(byteArrayInputStream, null);
            GenericData.Record avroRecord = new GenericData.Record(schema);
            GenericData.Record genericRecord = (GenericData.Record) genericDatumReader.read(avroRecord, binaryDecoder);
            System.out.println("time= " + genericRecord.get("time")
                    + ", site= " + genericRecord.get("site")
                    + ", ip=" + genericRecord.get("ip"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
