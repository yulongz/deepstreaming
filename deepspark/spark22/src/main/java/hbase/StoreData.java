package hbase;

import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import utils.JsonUtil;
import utils.PropsUtil;

import java.io.IOException;

/**
 * @author Tiny
 * @date 2017/11/16
 */
public class StoreData {
    private static Connection connection;
    private static Configuration conf;
    private static TableName name;

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

    /**
     * 原始数据落地
     * */
    public static void storeData(String str,String tName) throws IOException {
        JSONObject jsonObject = JsonUtil.fromJson(str);
        Long unixTime = getUnixTime(str);

        //yulongz-dictb
        name = TableName.valueOf(tName);
        Table table = connection.getTable(name);
        byte[] rowkey = Bytes.toBytes(unixTime);
        Put put = new Put(rowkey);

        for (Object key : jsonObject.keySet()){
            Object val = jsonObject.get(key);
            put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes(key.toString()),Bytes.toBytes(val.toString()));
        }
        table.put(put);
        table.close();
    }

    public static void storeData(String rowKey,String value,String tName) throws IOException {
        JSONObject jsonObject = JsonUtil.fromJson(value);
        //yulongz-dictb
        name = TableName.valueOf(tName);
        Table table = connection.getTable(name);
        byte[] rowkey = Bytes.toBytes(rowKey);
        Put put = new Put(rowkey);
        for (Object key : jsonObject.keySet()){
            Object val = jsonObject.get(key);
            put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes(key.toString()),Bytes.toBytes(val.toString()));
        }
        table.put(put);
        table.close();
    }

    public static Long getUnixTime(String str){
        JSONObject jsonObject = JsonUtil.fromJson(str);
        Long unixTime = (Long) jsonObject.get("UnixTime");
        Long key = Long.MAX_VALUE - unixTime;
        return key;
    }

    public static Long getReverseTime(String str){
        Long unixTime = Long.parseLong(str);
        Long key = Long.MAX_VALUE - unixTime;
        return key;
    }
}
