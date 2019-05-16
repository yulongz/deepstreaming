package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Project Name:chapter3
 * Package Name:org.smart4j.framework.util
 * Date:2017/9/14 16:54
 * AUTHOR by zhangyulong
 * Email:sky.zyl@hotmail.com
 */
public final class PropsUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);

    /*加载属性文件*/
    public static Properties loadProps(String fileName){
        Properties props = null;
        InputStream is = null;
        try{
            is = ClassUtil.getClassLoader().getResourceAsStream(fileName);
            if(is == null){
                throw new FileNotFoundException(fileName + " file is not fount");
            }
            props = new Properties();
            props.load(is);
        } catch (IOException e){
            LOGGER.error("load properties file failure", e);
        } finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("close input stream failure", e);
                }
            }
        }
        return props;
    }

    /*获取string类型的属性值(默认值是空)*/
    public static String getString(Properties props,String key){
        return getString(props,key,"");
    }

    /*获取String类型的属性值(提供默认值)*/
    public static String getString(Properties props,String key,String defaultValue){
        String value = defaultValue;
        if(props.containsKey(key)){
            value = props.getProperty(key);
        }
        return value;
    }

    /*获取int类型的属性值(默认值是0)*/
    public static int getInt(Properties props, String key){
        return getInt(props,key,0);
    }

    /*获取int类型的属性值(提供默认值)*/
    public static int getInt(Properties props, String key, int defaultValue){
        int value = defaultValue;
        if(props.containsKey(key)){
            value = CastUtil.castInt(props.getProperty(key));
        }
        return value;
    }

    /*获取boolean类型属性(默认值是false)*/
    public static boolean getBoolean(Properties props,String key){
        return getBoolean(props,key,false);
    }

    /*获取boolean类型属性(提供默认值)*/
    private static boolean getBoolean(Properties props, String key, boolean defaultValue) {
        boolean value = defaultValue;
        if(props.containsKey(key)){
            value = CastUtil.castBoolean(props.getProperty(key));
        }
        return value;
    }
}
