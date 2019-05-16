package com.yulongz.example.mapreduce;

import org.apache.hadoop.util.ToolRunner;
import org.junit.Test;

/**
 * @author Tiny
 * @date 2017/12/18
 */
public class MapReduceColorCountTest {
    @Test
    public void testMapReduceColorCount() throws Exception {
        String[] args = new String[2];
        args[0] = "/home/kafka/IdeaProjects/bekdemo/src/main/resources/input/users.avro";
        args[1] = "/home/kafka/IdeaProjects/bekdemo/src/main/resources/output/";
        int res = ToolRunner.run(new MapReduceColorCount(), args);
        System.exit(res);
    }
}
