/** 
 * Project Name:stormlearn 
 * File Name:NothingBaseBasicBolt.java 
 * Package Name:stormlearn.component.grouping 
 * Date:2017年8月6日下午4:38:13 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.component.grouping;

import java.util.Map;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * ClassName:NothingBaseBasicBolt <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月6日 下午4:38:13 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings({ "rawtypes", "serial" })
public class NothingBaseBasicBolt extends BaseBasicBolt {
	private int _taskId;
	private String _componentId;

	public void prepare(Map stormConf, TopologyContext context) {
		this._taskId = context.getThisTaskId();
		this._componentId = context.getThisComponentId();
	};

	public void execute(Tuple input, BasicOutputCollector collector) {
		System.err.println("componentId:" + _componentId + ",taskId:" + this._taskId + ".");
		collector.emit(new Values(input.getString(0)));
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

}
