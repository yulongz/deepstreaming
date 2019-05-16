package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Project Name:chapter3
 * Package Name:org.smart4j.framework.util
 * Date:2017/9/15 9:31
 * AUTHOR by zhangyulong
 * Email:sky.zyl@hotmail.com
 */
public final class CastUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CastUtil.class);

    /*转成String类型*/
    public static String castString(Object obj){
        return CastUtil.castString(obj,"");
    }

    /*转成String类型(提供默认值)*/
    private static String castString(Object obj, String defaultValue) {
        return obj != null ?String.valueOf(obj):defaultValue;
    }

    /*转成Double类型*/
    public static double castDouble(Object obj){
        return CastUtil.castDouble(obj,0);
    }

    /*转成Double类型(提供默认值)*/
    private static double castDouble(Object obj, double defaultValue) {
        double doubleValue = defaultValue;
        if(obj != null){
            String strValue = castString(obj);
            if(StringUtil.isNotEmpty(strValue)){
                try{
                    doubleValue = Double.parseDouble(strValue);
                }catch (NumberFormatException e){
                    LOGGER.warn("castDouble fail",e);
                    doubleValue =  defaultValue;
                }
            }
        }
        return doubleValue;
    }

    /*转成Long类型*/
    public static long castLong(Object obj){
        return CastUtil.castLong(obj,0);
    }

    /*转成Long类型(提供默认值)*/
    private static long castLong(Object obj, long defaultValue) {
        Long longValue = defaultValue;
        if(obj != null){
            String strValue = castString(obj);
            if(StringUtil.isNotEmpty(strValue)){
                try{
                    longValue = Long.parseLong(strValue);
                }catch(NumberFormatException e){
                    LOGGER.warn("castLong fail",e);
                    longValue = defaultValue;
                }
            }
        }
        return longValue;
    }

    /*转为int类型*/
    public static int castInt(Object obj){
        return CastUtil.castInt(obj,0);
    }

    /*转为int类型*/
    private static int castInt(Object obj, int defaultValue) {
        int intValue = defaultValue;
        if(obj != null){
            String strValue = castString(obj);
            if(StringUtil.isNotEmpty(strValue)){
                try{
                    intValue = Integer.parseInt("strValue");
                } catch (NumberFormatException e){
                    LOGGER.warn("castInteger fail",e);
                    intValue = defaultValue;
                }
            }
        }
        return intValue;
    }

    /*转成boolean类型*/
    public static boolean castBoolean(Object obj){
        return CastUtil.castBoolean(obj,false);
    }

    /*转成boolean类型(提供默认值)*/
    private static boolean castBoolean(Object obj, boolean defaultValue) {
        boolean booleanValue = defaultValue;
        if(obj != null){
            booleanValue = Boolean.parseBoolean(castString(obj));
        }
        return booleanValue;
    }
}
