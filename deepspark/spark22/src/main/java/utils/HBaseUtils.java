package utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;

/**
 * @author Tiny
 * @date 2017/11/16
 */
public class HBaseUtils {
    private static Connection connection;
    private static Configuration conf;

    static {
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum",
                PropsUtil.loadProps("my.properties").getProperty("hbase.zookeeper.quorum"));
        conf.set("hbase.zookeeper.property.clientPort",
                PropsUtil.loadProps("my.properties").getProperty("hbase.zookeeper.property.clientPort"));

        try {
            connection = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() throws IOException {
        if (connection != null) {
            connection.close();
        }
    }
}
