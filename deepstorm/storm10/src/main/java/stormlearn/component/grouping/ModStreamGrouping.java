/** 
 * Project Name:stormlearn 
 * File Name:ModStreamGrouping.java 
 * Package Name:stormlearn.grouping 
 * Date:2017年8月6日下午3:55:58 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.component.grouping;

import java.util.Arrays;
import java.util.List;

import org.apache.storm.generated.GlobalStreamId;
import org.apache.storm.grouping.CustomStreamGrouping;
import org.apache.storm.task.WorkerTopologyContext;
import org.apache.storm.tuple.Fields;

/**
 * ClassName:ModStreamGrouping <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月6日 下午3:55:58 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings({ "unused", "serial" })
public class ModStreamGrouping implements CustomStreamGrouping {

	private WorkerTopologyContext _ctx;
	private GlobalStreamId _stream;
	private List<Integer> _targetTasks;
	private String _StromId;
	private Fields outFields;
	private Fields fields;
	private List<Object> selectedFields;

	public ModStreamGrouping(Fields fields) {
		this.fields = fields;
	}

	@Override
	public void prepare(WorkerTopologyContext context, GlobalStreamId stream, List<Integer> targetTasks) {
		this._ctx = context;
		this._stream = stream;
		this._targetTasks = targetTasks;
		this._StromId = context.getStormId();
		this.outFields = context.getComponentOutputFields(stream);

	}

	@Override
	public List<Integer> chooseTasks(int taskId, List<Object> values) {
		selectedFields = outFields.select(fields, values);
		Long groupingKey = Long.valueOf(selectedFields.get(0).toString());
		int index = (int) (groupingKey % (_targetTasks.size()));
		System.out.println("[stormId:" + _StromId + "][targetTasksSize:" + _targetTasks.size() + "][groupingKey:"
				+ groupingKey + "][index:" + index + "]");
		return Arrays.asList(_targetTasks.get(index));
	}

}
