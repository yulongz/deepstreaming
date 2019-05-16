package utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Project Name:chapter3
 * Package Name:org.smart4j.framework.util
 * Date:2017/9/17 19:41
 * AUTHOR by zhangyulong
 * Email:sky.zyl@hotmail.com
 */
public final class CodecUtil {

    private  static final Logger LOGGER = LoggerFactory.getLogger(CodecUtil.class);

    //将URL编码
    public static String encodeURL(String source){
        String target;
        try {
            target = URLEncoder.encode(source,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("encode url failure",e);
            throw new RuntimeException(e);
        }
        return target;
    }

    //将URL解码
    public static String decodeURL(String source){
        String target;
        try {
            target = URLDecoder.decode(source,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("decode url failure",e);
            throw new RuntimeException(e);
        }
        return target;
    }

    //MD5加密
    public static String md5(String source){
        return DigestUtils.md5Hex(source);
    }
}
