/** 
 * Project Name:stormlearn 
 * File Name:BaseRichSpoutImpl.java 
 * Package Name:stormlearn.component.baserich 
 * Date:2017年8月2日上午9:48:55 
 * sky.zyl@hotmail.com
*/  
  
package stormlearn.component.baserich;

import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;

/** 
 * ClassName:BaseRichSpoutImpl <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年8月2日 上午9:48:55 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
@SuppressWarnings("serial")
public class BaseRichSpoutImpl extends BaseRichSpout{

	@SuppressWarnings("rawtypes")
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
	}

	public void nextTuple() {
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

}
 