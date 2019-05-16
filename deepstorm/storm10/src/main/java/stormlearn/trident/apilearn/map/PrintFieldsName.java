/** 
 * Project Name:stormlearn 
 * File Name:PrintFieldsName.java 
 * Package Name:stormlearn.trident.apilearn.map 
 * Date:2017年8月6日上午1:10:08 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.map;

import java.util.Iterator;
import org.apache.storm.trident.operation.MapFunction;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Values;

/**
 * ClassName:PrintFieldsName <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月6日 上午1:10:08 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings("serial")
public class PrintFieldsName implements MapFunction {
	private Values values;

	public Values execute(TridentTuple input) {
		System.err.print("TridentTuple Size:" + input.size() + ".FieldsList:");
		Iterator<String> itr = input.getFields().iterator();
		while (itr.hasNext()) {
			String fieldName = itr.next();
			System.err.print("[" + fieldName + "]");
		}
		System.err.println();
		values = new Values();
		for (int i = 0; i < input.size(); i++) {
			values.add(input.getValue(i));
		}
		return values;
	}
}
