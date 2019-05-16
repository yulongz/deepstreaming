/** 
 * Project Name:stormlearn 
 * File Name:PrintBaseBasicBolt.java 
 * Package Name:stormlearn.component.basebasic 
 * Date:2017年8月2日下午4:47:01 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.component.grouping;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

/**
 * ClassName:PrintBaseBasicBolt <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月2日 下午4:47:01 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings("serial")
public class PrintBaseBasicBolt extends BaseBasicBolt {
	private String fields;

	public void execute(Tuple input, BasicOutputCollector collector) {
		fields = "";
		for (int i = 0; i < input.size(); i++) {
			fields = fields + "[" + i + ":" + input.getValue(i) + "]";
		}
		System.out.println(fields);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}
}
