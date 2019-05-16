package hbase;

import org.junit.Test;
import utils.HBaseUtils;
import utils.PropsUtil;

import java.io.IOException;
import java.util.Map;

import static hbase.MappingTable.*;

public class MappingTableTest {

    @Test
    public void mappingTableTest() throws IOException {
        String tableName = PropsUtil.loadProps("my.properties").getProperty("mapping.table");
        Map<String, Map<String, String>> data2Map = hbase2Map(tableName);
        Map<String, String> dataFromMap = getDataFromMap("DCS:AI:10HLF10CF101", getSet(), data2Map);
        traverseMap(dataFromMap);
        HBaseUtils.close();
    }
}