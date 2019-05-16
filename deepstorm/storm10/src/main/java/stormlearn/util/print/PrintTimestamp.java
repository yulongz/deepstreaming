/** 
 * Project Name:stormlearn 
 * File Name:PrintTimestamp.java 
 * Package Name:stormlearn.util 
 * Date:2017年8月3日下午4:59:00 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/  
  
package stormlearn.util.print;

import java.text.SimpleDateFormat;
import java.util.Date;

/** 
 * ClassName:PrintTimestamp <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年8月3日 下午4:59:00 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
public class PrintTimestamp {
	 
	public static String getTSString(long timeStamp){
		 Date nowTime = new Date(timeStamp);
		 SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		 return sdFormatter.format(nowTime);
	}
	public static String getTSString(){
		 Date nowTime = new Date(System.currentTimeMillis());
		 SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		 return sdFormatter.format(nowTime);
	}
}
 