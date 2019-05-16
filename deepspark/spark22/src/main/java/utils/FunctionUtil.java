package utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.DoubleAccumulator;

/**
 * @author Tiny
 * @date 2017/11/17
 */
public class FunctionUtil {
    private static DecimalFormat df = new DecimalFormat("###0.00");

    public static String sum(List<String> list){
        double sum = 0;
        for (String str : list){
            if (StringUtil.isNotEmpty(str)){
                double s;
                s = Double.parseDouble(str);
                sum += s;
            }
        }
        return df.format(sum);
    }

    public static int count(List<String> list){
        int size = list.size();
        return size;
    }

    public static int countNotNull(List<String> list){
        List<Double> notNullList = new ArrayList<>();
        for (String str : list){
            if (StringUtil.isNotEmpty(str)){
                double s = Double.parseDouble(str);
                if (s != 0) {
                    notNullList.add(s);
                }
            }
        }
        int size = notNullList.size();
        return size;
    }

    public static String avg(List<String> list){
        double avg = Double.parseDouble(sum(list)) / count(list);
        return df.format(avg);
    }

    public static String avgNotNull(List<String> list){
        double avg = Double.parseDouble(sum(list)) / countNotNull(list);
        return df.format(avg);
    }

    public static double max(List<String> list){
        List<Double> doubleList = new ArrayList<>();
        for (String str : list) {
            if (StringUtil.isNotEmpty(str)){
                double s = Double.parseDouble(str);
                doubleList.add(Double.parseDouble(str));
            }
        }

        return Collections.max(doubleList);
    }

    public static double maxNotNull(List<String> list){
        List<Double> doubleList = new ArrayList<>();
        for (String str : list) {
            if (StringUtil.isNotEmpty(str)){
                double s = Double.parseDouble(str);
                if (s != 0) {
                    doubleList.add(s);
                }
            }
        }

        return Collections.max(doubleList);
    }


    public static double min(List<String> list){
        List<Double> doubleList = new ArrayList<>();
        for (String str : list) {
            if (StringUtil.isNotEmpty(str)){
                doubleList.add(Double.parseDouble(str));
            }
        }

        return Collections.min(doubleList);
    }

    public static double minNotNull(List<String> list){
        List<Double> doubleList = new ArrayList<>();
        for (String str : list) {
            if (StringUtil.isNotEmpty(str)){
                double s = Double.parseDouble(str);
                if (s != 0){
                    doubleList.add(s);
                }
            }
        }
        return Collections.min(doubleList);
    }

    public static String digit(double d, int num){
        DecimalFormat format = null;
        String str = null;
        if (num == 2){
            format = new DecimalFormat("###0.00");
            str = format.format(d);
        } else if (num == 3){
            format = new DecimalFormat("###0.000");
            str = format.format(d);
        } else if (num == 4){
            format = new DecimalFormat("###0.0000");
            str = format.format(d);
        }
        return str;
    }
}
