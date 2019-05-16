/** 
 * Project Name:stormlearn 
 * File Name:IWindowedBoltImpl.java 
 * Package Name:stormlearn.windows 
 * Date:2017年8月2日下午8:28:01 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/  
  
package stormlearn.component.windows;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IWindowedBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.windowing.TupleWindow;

/** 
 * ClassName:IWindowedBoltImpl <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年8月2日 下午8:28:01 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
@SuppressWarnings("serial")
public class IWindowedBoltImpl implements IWindowedBolt{

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

	@SuppressWarnings("rawtypes")
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
	}

	public void execute(TupleWindow inputWindow) {
	}

	public void cleanup() {
	}

}
 