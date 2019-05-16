/** 
 * Project Name:stormlearn 
 * File Name:BaseRichBoltImpl.java 
 * Package Name:stormlearn.component.baserich 
 * Date:2017年8月2日上午9:53:00 
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
 * ClassName:BaseRichBoltImpl <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年8月2日 上午9:53:00 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
@SuppressWarnings("serial")
public class BaseRichBoltImpl extends BaseRichBolt{

	@SuppressWarnings("rawtypes")
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
	}

	public void execute(Tuple input) {
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

}
 