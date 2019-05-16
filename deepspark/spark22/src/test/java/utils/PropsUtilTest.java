package utils;

import org.junit.Test;

import java.util.Properties;

public class PropsUtilTest {

    @Test
    public void propsUtilsTest(){
        Properties properties = PropsUtil.loadProps("my.properties");
        String sourceTopic = PropsUtil.getString(properties, "sourcetopic");
        String groupId = PropsUtil.getString(properties, "group.id");
        System.out.println("sourcetopic:"+sourceTopic);
        System.out.println("group.id:"+groupId);
    }

}
