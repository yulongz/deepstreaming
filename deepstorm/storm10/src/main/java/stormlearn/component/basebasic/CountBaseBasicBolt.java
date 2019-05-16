/** 
 * Project Name:stormlearn 
 * File Name:CountBaseBasicBolt.java 
 * Package Name:stormlearn.component.basebasic 
 * Date:2017年8月2日下午4:38:08 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.component.basebasic;

import java.util.HashMap;

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
@SuppressWarnings("serial")
public class CountBaseBasicBolt extends BaseBasicBolt {

	private String word;
	private HashMap<String, Integer> counter = new HashMap<String, Integer>();
	private Integer count;

	public void execute(Tuple input, BasicOutputCollector collector) {
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
