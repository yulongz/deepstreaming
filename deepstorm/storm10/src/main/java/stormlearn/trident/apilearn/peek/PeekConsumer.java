/** 
 * Project Name:stormlearn 
 * File Name:PeekConsumer.java 
 * Package Name:stormlearn.trident.apilearn.peek 
 * Date:2017年8月7日下午8:47:55 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.peek;

import org.apache.storm.trident.operation.Consumer;
import org.apache.storm.trident.tuple.TridentTuple;

import stormlearn.util.print.PrintHelper;

/**
 * ClassName:PeekConsumer <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月7日 下午8:47:55 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings("serial")
public class PeekConsumer implements Consumer {
	private String fields;
	private boolean isDebug;

	public PeekConsumer() {
		this.isDebug = false;
	}

	public PeekConsumer(boolean isDebug) {
		this.isDebug = true;
	}

	@Override
	public void accept(TridentTuple input) {
		fields = "";
		for (int i = 0; i < input.size(); i++) {
			fields = fields + "[" + i + ":" + input.getValue(i) + "]";
		}
		if (isDebug) {
			PrintHelper.print(fields);
		} else {
			System.err.println(fields);
		}
	}
}
