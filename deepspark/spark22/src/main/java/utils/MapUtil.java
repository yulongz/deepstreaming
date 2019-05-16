package utils;

import java.util.Map;

public class  MapUtil {

    public static <K,V> Object[][] mapToArray(Map<K,V> map){

        Object[][] twoDarray = new Object[map.size()][2];

        Object[] keys = map.keySet().toArray();
        Object[] values = map.values().toArray();

        for (int row = 0; row < twoDarray.length; row++) {
            twoDarray[row][0] = keys[row];
            twoDarray[row][1] = values[row];
        }
        return twoDarray;
    }
}
