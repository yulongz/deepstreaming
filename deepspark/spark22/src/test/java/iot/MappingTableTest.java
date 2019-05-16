package iot;

import hbase.MappingTable;
import org.junit.Test;
import utils.PropsUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class MappingTableTest {

    @Test
    public void getDataTest(){
        //Set<String> set, String oldCode
        HashSet<String> strings = new HashSet<>();
        String tableName = PropsUtil.loadProps("my.properties").getProperty("mapping.table");

        strings.add("newCode");
        try {
            System.out.println(MappingTable.getData(strings,"DCS:AI:10HLF10CF101",tableName).get("newCode"));
            System.out.println(MappingTable.getData(strings,"123",tableName).get("newCode"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
