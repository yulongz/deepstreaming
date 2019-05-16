/** 
 * Project Name:stormlearn 
 * File Name:WordCountBolt1.java 
 * Package Name:stormlearn.component 
 * Date:2017年7月27日上午9:09:31 
 * sky.zyl@hotmail.com
*/  
  
package stormlearn.component.irich;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/** 
 * ClassName:WordCountBolt1 <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年7月27日 上午9:09:31 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
@SuppressWarnings("serial")
public class CountIRichBolt implements IRichBolt{
	
	private OutputCollector collector;
	private HashMap<String, Integer> counts;

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
		collector.ack(input);
	}

	public void cleanup() {
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word", "count"));
	}

	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
	

}
 