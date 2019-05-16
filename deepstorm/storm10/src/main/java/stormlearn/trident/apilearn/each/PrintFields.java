/** 
 * Project Name:stormlearn 
 * File Name:PrintOneString.java 
 * Package Name:stormlearn.trident.apilearn.basefunction 
 * Date:2017年8月4日下午8:59:22 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.each;

import java.util.Map;

import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.operation.TridentOperationContext;
import org.apache.storm.trident.tuple.TridentTuple;

import stormlearn.util.print.PrintHelper;

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
public class PrintFields extends BaseFunction {
	private boolean isDebug;
	private Object fields;
	private int partitionIndex;

	public PrintFields() {
		this.isDebug = false;
	}

	public PrintFields(boolean isDebug) {
		this.isDebug = isDebug;
	}

	@SuppressWarnings("rawtypes")
	public void prepare(Map conf, TridentOperationContext context) {
		this.partitionIndex = context.getPartitionIndex();
	}

	public void execute(TridentTuple tuple, TridentCollector collector) {
		fields = "";
		for (int i = 0; i < tuple.size(); i++) {
			fields = fields+"[" + i + ":" + tuple.getValue(i) + "]";
		}
		if (isDebug) {
			PrintHelper.print("[" + partitionIndex + "] " + fields);
		} else {
			System.out.println(fields);
		}
	}
}
