/** 
 * Project Name:stormlearn 
 * File Name:PrintHelper.java 
 * Package Name:stormlearn.component 
 * Date:2017年8月2日上午9:25:13 
 * sky.zyl@hotmail.com
*/

package stormlearn.util.print;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ClassName:PrintHelper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月2日 上午9:25:13 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class PrintHelper {

	private static SimpleDateFormat sf = new SimpleDateFormat("mm:ss:SSS");

	public static void print(String out) {
		System.err.println(sf.format(new Date()) + " [" + Thread.currentThread().getName() + "] " + out);
	}

}
