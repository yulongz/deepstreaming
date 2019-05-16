/** 
 * Project Name:stormlearn 
 * File Name:MyFilter.java 
 * Package Name:stormlearn.trident.apilearn.basefilter 
 * Date:2017年8月5日下午8:51:05 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.filter;

import org.apache.storm.trident.operation.BaseFilter;
import org.apache.storm.trident.tuple.TridentTuple;

/**
 * ClassName:MyFilter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月5日 下午8:51:05 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings("serial")
public class MyFilter extends BaseFilter {
	private String fields;

	public boolean isKeep(TridentTuple tuple) {
		for (int i = 0; i < tuple.size(); i++) {
			fields = "" + tuple.getValue(i);
		}
		if (fields.length() == 0)
			return false;
		return fields.charAt(0) == 't';
	}
}