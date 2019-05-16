package utils;

import org.junit.Test;

/**
 * @author Tiny
 * @date 2017/11/20
 */
public class TimeUtilTest {
    @Test
    public void timeTest(){
        long l = System.currentTimeMillis();
        System.out.println(l);
        System.out.println(TimeUtil.unix2Utc(l));
        System.out.println(TimeUtil.utc2Unix(TimeUtil.unix2Utc(l)));
    }
}
