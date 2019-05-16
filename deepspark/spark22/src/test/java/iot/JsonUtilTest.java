package iot;

import com.alibaba.fastjson.JSONObject;
import hbase.MappingTable;
import model.OrginRecord;
import org.junit.Test;
import utils.JsonUtil;
import utils.PropsUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

public class JsonUtilTest {

    @Test
    public void fromJsonTest1() {
        String json = "{\"UTCTime\":\"2017-11-15T14:35:09.392Z\",\"UnixTime\":\"1510727709407\",\"DCS:AI:10HLF10CF101\":\"252.50\",\"DCS:AI:10HLF10CF101F\":\"228.63\",\"DCS:AI:10HLF10CF102\":\"152.55\",\"DCS:AI:10HLF10CF102F\":\"170.08\",\"DCS:AI:10HLF10CF103\":\"171.73\",\"DCS:AI:10HLF10CF103F\":\"239.79\",\"DCS:AI:10HLF20CF101\":\"209.91\",\"DCS:AI:10HLF20CF101F\":\"199.30\",\"DCS:AI:10HLF20CF102\":\"182.59\",\"DCS:AI:10HLF20CF102F\":\"245.14\",\"DCS:AI:10HLF20CF103\":\"295.64\",\"DCS:AI:10HLF20CF103F\":\"183.34\"}";
        OrginRecord orginRecord = JsonUtil.fromJson(json, OrginRecord.class);
        System.out.println(orginRecord.toString());
    }

    @Test
    public void fromJsonTest2() {
        String json = "{\"UTCTime\":\"2017-11-15T14:35:09.392Z\",\"UnixTime\":\"1510727709407\",\"DCS:AI:10HLF10CF101\":\"252.50\",\"DCS:AI:10HLF10CF101F\":\"228.63\",\"DCS:AI:10HLF10CF102\":\"152.55\",\"DCS:AI:10HLF10CF102F\":\"170.08\",\"DCS:AI:10HLF10CF103\":\"171.73\",\"DCS:AI:10HLF10CF103F\":\"239.79\",\"DCS:AI:10HLF20CF101\":\"209.91\",\"DCS:AI:10HLF20CF101F\":\"199.30\",\"DCS:AI:10HLF20CF102\":\"182.59\",\"DCS:AI:10HLF20CF102F\":\"245.14\",\"DCS:AI:10HLF20CF103\":\"295.64\",\"DCS:AI:10HLF20CF103F\":\"183.34\"}";
        JSONObject jsonObject = JsonUtil.fromJson(json);
        System.out.println(jsonObject.toJSONString());
    }

    @Test
    public void transformKeysTest() {
        String json = "{\"UTCTime\":\"2017-11-15T14:35:09.392Z\",\"UnixTime\":\"1510727709407\",\"DCS:AI:10HLF10CF101\":\"252.50\",\"DCS:AI:10HLF10CF101F\":\"228.63\",\"DCS:AI:10HLF10CF102\":\"152.55\",\"DCS:AI:10HLF10CF102F\":\"170.08\",\"DCS:AI:10HLF10CF103\":\"171.73\",\"DCS:AI:10HLF10CF103F\":\"239.79\",\"DCS:AI:10HLF20CF101\":\"209.91\",\"DCS:AI:10HLF20CF101F\":\"199.30\",\"DCS:AI:10HLF20CF102\":\"182.59\",\"DCS:AI:10HLF20CF102F\":\"245.14\",\"DCS:AI:10HLF20CF103\":\"295.64\",\"DCS:AI:10HLF20CF103F\":\"183.34\"}";
        String tableName = PropsUtil.loadProps("my.properties").getProperty("mapping.table");
        JSONObject jsonObject = JsonUtil.fromJson(json);
        JSONObject jsonObjectNew = new JSONObject();
        jsonObjectNew.put("UTCTime",jsonObject.get("UTCTime"));
        jsonObjectNew.put("UnixTime",jsonObject.get("UnixTime"));
        Iterator<String> iterator = jsonObject.keySet().iterator();
        HashSet<String> strings = new HashSet<>();
        strings.add("newCode");
        String newKey;
        while (iterator.hasNext()) {
            String oldKey = iterator.next();
            try {
                newKey = MappingTable.getData(strings, oldKey, tableName).get("newCode");
                if(newKey == null){
                    System.out.println(oldKey+"null:"+newKey);
                }else{
                    System.out.println(oldKey+"notnull:"+newKey);
                    jsonObjectNew.put(newKey,jsonObject.get(oldKey));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(jsonObject.toJSONString());
        System.out.println(jsonObjectNew.toJSONString());
    }

}
