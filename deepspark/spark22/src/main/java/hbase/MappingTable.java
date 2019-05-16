package hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import utils.HBaseUtils;
import utils.PropsUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Tiny
 * @date 2017/11/15
 */
public class MappingTable {
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
     * 从HBase获取数据
     */

    public static Map<String, String> getData(Set<String> set, String oldCode, String tName) throws IOException {
        Map<String, String> map = new HashMap<>();
        name = TableName.valueOf(tName);
        Table table = connection.getTable(name);
        byte[] rowkey = Bytes.toBytes(oldCode);
        Get get = new Get(rowkey);
        Result result;

        for (String str : set) {
            get.addColumn(Bytes.toBytes("cf"), Bytes.toBytes(str));
            result = table.get(get);
            byte[] b = result.getValue(Bytes.toBytes("cf"), Bytes.toBytes(str));

            map.put(str, Bytes.toString(b));
        }

        table.close();
        return map;
    }


    /**
     * 将HBase数据加载到内存里
     */
    public static Map<String, Map<String, String>> hbase2Map(String tName) throws IOException {

        Map<String, Map<String, String>> rowMap = new HashMap<>();
        Map<String, String> colMap = new HashMap<>();


        name = TableName.valueOf(tName);
        Table table = connection.getTable(name);

        byte[] start = Bytes.toBytes(0);
        byte[] end = Bytes.toBytes("zZ");

        Scan scan = new Scan(start, end);

        ResultScanner rs = table.getScanner(scan);

        for (Result r : rs) {
            Cell[] cells = r.rawCells();
            for (Cell cell : cells) {
                colMap.put(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)));
                rowMap.put(new String(CellUtil.cloneRow(cell)), colMap);
            }
        }

        rs.close();
        table.close();

        return rowMap;
    }

    /***
     *从map里查询数据
     */
    public static Map<String, String> getDataFromMap(String oldCode, Set<String> set, Map<String, Map<String, String>> rowMap) throws IOException {
        Map<String, String> valMap = new HashMap<>();
        Map<String, String> colMap = rowMap.get(oldCode);
        ;

        for (String str : set) {
            valMap.put(str, colMap.get(str));
        }

        return valMap;
    }

    public static <K, V> void traverseMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {

            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

        }
    }

    public static Set<String> getSet() {
        Set<String> set = new HashSet<>();
        set.add("newCode");
        set.add("codeName");
        set.add("codeID");
        set.add("unit");
        set.add("type");
        set.add("accuracy");
        set.add("freq");
        set.add("desc");
        set.add("province");
        set.add("provinceCode");
        set.add("factoryName");
        set.add("factoryNameCode");
        set.add("factoryType");
        set.add("factoryTypeCode");
        set.add("device");
        set.add("deviceCode");
        set.add("number");
        set.add("numCode");
        set.add("systemFir");
        set.add("systemFirCode");
        set.add("systemSec");
        set.add("systemSecCode");
        set.add("systemThr");
        set.add("systemThrCode");
        set.add("systemNum");
        set.add("systemNumCode");
        set.add("equipType");
        set.add("equipTpyeCode");
        set.add("equip");
        set.add("equipCode");
        set.add("partFir");
        set.add("partFirCode");
        set.add("partSec");
        set.add("partSecCode");
        set.add("mpOrderNum");
        set.add("mpOrderNumCode");
        set.add("dataSourceType");
        set.add("dataSourceTypeCode");
        set.add("dataSourceCode");
        set.add("dataSourceCodeCode");

        return set;
    }
}
