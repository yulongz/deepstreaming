package com.yulongz.example.mapred;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.junit.Test;

/**
 * @author Tiny
 * @date 2017/12/18
 */
public class MapredColorCountTest {
    @Test
    public void testMapredColorCount() throws Exception {
        String[] args = new String[2];
        args[0] = "/home/kafka/IdeaProjects/bekdemo/src/main/resources/input/users.avro";
        args[1] = "/home/kafka/IdeaProjects/bekdemo/src/main/resources/output/";
        int res = ToolRunner.run(new Configuration(), new MapredColorCount(), args);
        System.exit(res);
    }
}
