/** 
 * Project Name:stormlearn 
 * File Name:PrintoutBolt.java 
 * Package Name:stormlearn.component 
 * Date:2017年7月25日下午5:20:32 
 * sky.zyl@hotmail.com
*/

package stormlearn.component.baserich;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

/**
 * ClassName:PrintoutBolt <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月25日 下午5:20:32 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class PrintBaseRichBolt extends BaseRichBolt {

	/** 
	 * serialVersionUID:TODO
	 */  
	private static final long serialVersionUID = 3895782580110558225L;
	private OutputCollector collector;
	
	@SuppressWarnings("rawtypes")
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector=collector;
	}

	public void execute(Tuple input) {
		String word = input.getStringByField("word");
		Integer count = input.getIntegerByField("count");
		System.out.println(word + ":" + count);
		this.collector.ack(input);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}
}
