package com.yulongz.kafka.record.storm.trident;

import org.apache.avro.generic.GenericRecord;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;

/**
 * Created by kafka on 17-12-23.
 */
public class AvroFunctionTest extends BaseFunction {
    @Override
    public void execute(TridentTuple tridentTuple, TridentCollector tridentCollector) {
        GenericRecord value = (GenericRecord) tridentTuple.getValueByField("value");
        System.out.println(value.toString());
    }
}
