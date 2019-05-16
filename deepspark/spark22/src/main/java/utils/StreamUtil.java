package utils;

import org.apache.commons.io.output.StringBuilderWriter;
import org.omg.CORBA.portable.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Project Name:chapter3
 * Package Name:org.smart4j.framework.util
 * Date:2017/9/17 19:37
 * AUTHOR by zhangyulong
 * Email:sky.zyl@hotmail.com
 */
public final class StreamUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamUtil.class);

    //从输入流中获取字符串
    public static String getString(InputStream is){
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
        } catch (IOException e) {
            LOGGER.error("get string failure",e);
            throw new RuntimeException(e);
        }
        return sb.toString();
    }


    public static String getString(ServletInputStream is) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
        } catch (IOException e) {
            LOGGER.error("get string failure",e);
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
