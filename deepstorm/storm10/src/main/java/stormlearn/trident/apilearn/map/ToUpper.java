/** 
 * Project Name:stormlearn 
 * File Name:PrintOneString.java 
 * Package Name:stormlearn.trident.apilearn.basefunction 
 * Date:2017年8月4日下午8:59:22 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.map;

import org.apache.storm.trident.operation.MapFunction;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Values;

/**
 * ClassName:PrintOneString <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月4日 下午8:59:22 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings("serial")
public class ToUpper implements MapFunction {
	private Values values;

	public Values execute(TridentTuple input) {
		values = new Values();
		for (int i = 0; i < input.size(); i++) {
			if (input.getValue(i) instanceof String) {
				values.add(input.getString(0).toUpperCase());
			} else {
				values.add((input.getValue(i)));
			}
		}
		return values;
	}
}
