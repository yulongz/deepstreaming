/** 
 * Project Name:stormlearn 
 * File Name:PrintOneString.java 
 * Package Name:stormlearn.trident.apilearn.basefunction 
 * Date:2017年8月4日下午8:59:22 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.map;

import java.util.Map;
import org.apache.storm.trident.operation.MapFunction;
import org.apache.storm.trident.operation.TridentOperationContext;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Values;

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
public class PrintFields implements MapFunction {
	private boolean isDebug;
	private Object fields;
	private int partitionIndex;
	private Values values;
	private int numPartitions;

	public PrintFields() {
		this.isDebug = false;
	}

	public PrintFields(boolean isDebug) {
		this.isDebug = isDebug;
	}

	@SuppressWarnings("rawtypes")
	public void prepare(Map conf, TridentOperationContext context) {
		this.partitionIndex = context.getPartitionIndex();
		this.numPartitions = context.numPartitions();
	}

	public Values execute(TridentTuple input) {
		values = new Values();
		fields = "";
		for (int i = 0; i < input.size(); i++) {
			fields = fields + "[" + i + ":" + input.getValue(i) + "]";
			values.add(input.getValue(i));
		}
		if (isDebug) {
			PrintHelper.print("["+this.numPartitions+"] [" + partitionIndex + "] " + fields);
		} else {
			System.out.println(fields);
		}
		return values;
	}
}
