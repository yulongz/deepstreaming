/** 
 * Project Name:stormlearn 
 * File Name:AddSuffix.java 
 * Package Name:stormlearn.trident.apilearn.basefunction 
 * Date:2017年8月5日下午9:20:57 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.each;

import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Values;

/**
 * ClassName:AddSuffix <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月5日 下午9:20:57 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings("serial")
public class AddSuffix extends BaseFunction {
	private String fields;
	private String suffix;

	public AddSuffix() {
		this.suffix = "";
	}

	public AddSuffix(String suffix) {
		this.suffix = suffix;
	}

	public void execute(TridentTuple tuple, TridentCollector collector) {
		for (int i = 0; i < tuple.size(); i++) {
			fields = "" + tuple.getValue(i);
		}
		collector.emit(new Values(fields + suffix));
	}
}
