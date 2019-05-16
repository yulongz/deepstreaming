package utils;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;

/**
 * Project Name:chapter3
 * Package Name:org.smart4j.framework.util
 * Date:2017/9/15 17:20
 * AUTHOR by zhangyulong
 * Email:sky.zyl@hotmail.com
 */
public final class ArrayUtil {

    /*判断数组是否非空*/
    public static boolean isNotEmpty(Object[] array) {
        return !ArrayUtils.isEmpty(array);
    }

    /*判断数组是否为空*/
    public static boolean isEmpty(Object[] array) {
        return ArrayUtils.isEmpty(array);
    }

    /*double 对象二维数组转数值二维数组*/
    public static double[][] DoubleTodouble(Object[][] doubles) {
        double[][] mathdouble = new double[doubles.length][2];
        for (int i = 0; i < doubles.length; i++) {
            for (int j = 0; j < doubles[i].length; j++) {
                mathdouble[i][j] = ((Double) doubles[i][j]).doubleValue();
            }
        }
        return mathdouble;
    }
}
