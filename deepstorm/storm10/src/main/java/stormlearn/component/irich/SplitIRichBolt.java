/** 
 * Project Name:stormlearn 
 * File Name:SplitSentenceBolt.java 
 * Package Name:stormlearn.component 
 * Date:2017年7月25日下午5:20:17 
 * sky.zyl@hotmail.com
*/

package stormlearn.component.irich;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * ClassName:SplitSentenceBolt <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月25日 下午5:20:17 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class SplitIRichBolt implements IRichBolt {
	/** 
	 * serialVersionUID:TODO
	 */  
	private static final long serialVersionUID = 3820372200857202619L;
	private OutputCollector collector;
	
	@SuppressWarnings("rawtypes")
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	public void execute(Tuple input) {
		String sentence = input.getString(0);
		for (String word : sentence.split(" ")) {
			collector.emit(input,new Values(word));
		}
		collector.ack(input);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

	public void cleanup() {
	}

	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
