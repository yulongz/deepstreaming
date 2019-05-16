/** 
 * Project Name:stormlearn 
 * File Name:BaseBasicImpl.java 
 * Package Name:stormlearn.component.basebasic 
 * Date:2017年8月2日下午5:06:28 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/  
  
package stormlearn.component.basebasic;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

/** 
 * ClassName:BaseBasicImpl <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年8月2日 下午5:06:28 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
@SuppressWarnings("serial")
public class BaseBasicImpl extends BaseBasicBolt{

	public void execute(Tuple input, BasicOutputCollector collector) {
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

}
 