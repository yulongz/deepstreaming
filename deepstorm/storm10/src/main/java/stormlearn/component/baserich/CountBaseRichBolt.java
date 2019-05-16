/** 
 * Project Name:stormlearn 
 * File Name:WordCountBolt.java 
 * Package Name:stormlearn.component 
 * Date:2017年7月25日下午5:20:25 
 * sky.zyl@hotmail.com
*/

package stormlearn.component.baserich;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * ClassName:WordCountBolt <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月25日 下午5:20:25 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class CountBaseRichBolt extends BaseRichBolt {

	/**
	 * serialVersionUID:TODO
	 */
	private static final long serialVersionUID = 6877839043885289229L;
	private OutputCollector collector;
	private Map<String, Integer> counts;

	@SuppressWarnings("rawtypes")
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.counts = new HashMap<String, Integer>();
	}

	public void execute(Tuple input) {
		String word = input.getString(0);
		Integer count = counts.get(word);
		if (count == null)
			count = 0;
		count++;
		counts.put(word, count);
		collector.emit(input,new Values(word, count));
		//this.collector.ack(input);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word", "count"));
	}
}
