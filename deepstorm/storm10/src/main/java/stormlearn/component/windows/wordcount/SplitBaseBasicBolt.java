/** 
 * Project Name:stormlearn 
 * File Name:SplitBaseBasicBolt.java 
 * Package Name:stormlearn.component.basebasic 
 * Date:2017年8月2日下午4:35:00 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/  
  
package stormlearn.component.windows.wordcount;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/** 
 * ClassName:SplitBaseBasicBolt <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年8月2日 下午4:35:00 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
@SuppressWarnings("serial")
public class SplitBaseBasicBolt extends BaseBasicBolt{

	public void execute(Tuple input, BasicOutputCollector collector) {
		String sentence = input.getString(0);
		for (String word : sentence.split(" ")) {
			collector.emit(new Values(word));
		}	
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

}
 