package hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import utils.PropsUtil;

import java.io.IOException;

/**
 * @author Tiny
 * @date 2017/11/16
 */
public class PutMappingData {
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
     * 码表插值
     * */
    public static void putData(String oldCode, String newCode, String codeName, String codeID, String systemNum, String systemNumCode,
                               String mpOrderNum, String mpOrderNumCode) throws IOException {

        name = TableName.valueOf(PropsUtil.loadProps("my.properties").getProperty("mapping.table"));
        Table table = connection.getTable(name);
        byte[] rowkey = Bytes.toBytes(oldCode);

        Put put = new Put(rowkey);
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("newCode"),Bytes.toBytes(newCode));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("codeName"),Bytes.toBytes(codeName));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("codeID"),Bytes.toBytes(codeID));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("unit"),Bytes.toBytes("km3/h"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("type"),Bytes.toBytes("float32"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("accuracy"),Bytes.toBytes("0")); //精度
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("freq"),Bytes.toBytes("0"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("desc"),Bytes.toBytes(""));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("province"),Bytes.toBytes("内蒙古自治区"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("provinceCode"),Bytes.toBytes("AE"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("factoryName"),Bytes.toBytes("华能上都电厂"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("factoryNameCode"),Bytes.toBytes("587"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("factoryType"),Bytes.toBytes("火电"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("factoryTypeCode"),Bytes.toBytes("F"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("device"),Bytes.toBytes("机组"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("deviceCode"),Bytes.toBytes("P"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("number"),Bytes.toBytes("1号"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("numCode"),Bytes.toBytes("1"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("systemFir"),Bytes.toBytes("锅炉系统"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("systemFirCode"),Bytes.toBytes("H"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("systemSec"),Bytes.toBytes("燃烧风系统"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("systemSecCode"),Bytes.toBytes("P"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("systemThr"),Bytes.toBytes("送风（二次风）机及系统"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("systemThrCode"),Bytes.toBytes("A"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("systemNum"),Bytes.toBytes(systemNum));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("systemNumCode"),Bytes.toBytes(systemNumCode));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("equipType"),Bytes.toBytes("HP"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("equipTpyeCode"),Bytes.toBytes("HP"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("equip"),Bytes.toBytes("风机出口风道"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("equipCode"),Bytes.toBytes("001"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("partFir"),Bytes.toBytes("不定义"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("partFirCode"),Bytes.toBytes("Z"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("partSec"),Bytes.toBytes("不定义"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("partSecCode"),Bytes.toBytes("Z"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("mpOrderNum"),Bytes.toBytes(mpOrderNum));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("mpOrderNumCode"),Bytes.toBytes(mpOrderNumCode));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("dataSourceType"),Bytes.toBytes("厂站接入点"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("dataSourceTypeCode"),Bytes.toBytes("J"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("dataSourceCode"),Bytes.toBytes("接入点0"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("dataSourceCodeCode"),Bytes.toBytes("0"));

        table.put(put);
        table.close();
    }
}
