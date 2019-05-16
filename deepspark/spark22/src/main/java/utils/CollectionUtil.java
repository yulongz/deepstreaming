package utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;
import java.util.Map;

/**
 * Project Name:chapter3
 * Package Name:org.smart4j.framework.util
 * Date:2017/9/15 16:05
 * AUTHOR by zhangyulong
 * Email:sky.zyl@hotmail.com
 */
public final class CollectionUtil {

    /*判断Collection是否为空*/
    public static boolean isEmpty(Collection<?> collection){
        return CollectionUtils.isEmpty(collection);
    }

    /*判断Collection是否非空*/
    public static boolean isNotEmpty(Collection<?> collection){
        return !isEmpty(collection);
    }

    /*判断Map
    是否非空*/
    public static boolean isEmpty(Map<?,?> map){
        return MapUtils.isEmpty(map);
    }

    /*判断Map是否非空*/
    public static boolean isNotEmpty(Map<?,?> map){
        return !isEmpty(map);
    }
}
