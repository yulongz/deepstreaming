package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Tiny
 * @date 2017/11/20
 */
public class TimeUtil {
    public static String unix2Utc(long unixTime) {
        String formats = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String utcTime = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(unixTime));
        return utcTime;
    }

    public static long utc2Unix(String utcTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            return df.parse(utcTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }
}
