/** 
 * Project Name:stormlearn 
 * File Name:NothingMap.java 
 * Package Name:stormlearn.trident.apilearn.map 
 * Date:2017年8月7日下午4:14:33 
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
 * ClassName:NothingMap <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月7日 下午4:14:33 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings("serial")
public class NothingMap implements MapFunction {
	private boolean isDebug;
	private Object fields;
	private int partitionIndex;
	private Values values;
	private int numPartitions;

	public NothingMap() {
		this.isDebug = false;
	}

	public NothingMap(boolean isDebug) {
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
			PrintHelper.print("[" + this.numPartitions + "] [" + partitionIndex + "] " + fields);
		} 
		return values;
	}

}
