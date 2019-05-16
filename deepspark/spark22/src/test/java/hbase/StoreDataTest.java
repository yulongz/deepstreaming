package hbase;

import org.junit.Test;
import utils.HBaseUtils;

import java.io.IOException;

import static hbase.StoreData.storeData;

public class StoreDataTest {

    @Test
    public void storeDataTest() throws IOException {
        String tableName="";
        storeData("",tableName);
        HBaseUtils.close();
    }
}
