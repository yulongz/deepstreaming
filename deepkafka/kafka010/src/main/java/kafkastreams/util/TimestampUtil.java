/** 
 * Project Name:kafkastreams 
 * File Name:TimestampUtil.java 
 * Package Name:kafkastreams.producer 
 * Date:2017年8月22日下午7:51:31 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package kafkastreams.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * ClassName:TimestampUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月22日 下午7:51:31 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class TimestampUtil {
	public static String TimestampFormat() {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String tsStr = "";
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss SSS");
		try {
			tsStr = sdf.format(ts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tsStr;
	}

	public static String TimestampFormat(long timestamp) {
		Timestamp ts = new Timestamp(timestamp);
		String tsStr = "";
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss SSSS");
		try {
			tsStr = sdf.format(ts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tsStr;
	}

}
