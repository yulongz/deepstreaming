/** 
 * Project Name:stormlearn 
 * File Name:CountBaseBasicBolt.java 
 * Package Name:stormlearn.component.basebasic 
 * Date:2017年8月2日下午4:38:08 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.component.grouping;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * ClassName:CountBaseBasicBolt <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月2日 下午4:38:08 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings({ "serial", "rawtypes","unused" })
public class CountBaseBasicBolt extends BaseBasicBolt {
	private int _taskId;
	private String _componentId;
	private String word;
	private HashMap<String, Integer> counter = new HashMap<String, Integer>();
	private Integer count;

	public void prepare(Map stormConf, TopologyContext context) {
		this._taskId = context.getThisTaskId();
		this._componentId = context.getThisComponentId();
	};

	public void execute(Tuple input, BasicOutputCollector collector) {
		//System.err.println("componentId:" + _componentId + ",taskId:" + this._taskId + ".");
		word = input.getString(0);
		count = counter.get(word);
		if (count == null)
			count = 0;
		count++;
		counter.put(word, count);
		collector.emit(new Values(word, count));
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word", "count"));
	}

}
