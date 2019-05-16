/** 
 * Project Name:stormlearn 
 * File Name:WCTopoBaseBasic.java 
 * Package Name:stormlearn.component.basebasic 
 * Date:2017年8月2日下午4:28:36 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.component.basebasic;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

/**
 * ClassName:WCTopoBaseBasic <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月2日 下午4:28:36 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class WCTopoBaseBasic {

	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("spout", new RandomBaseRichSpout(), 1);
		builder.setBolt("split", new SplitBaseBasicBolt(), 1).shuffleGrouping("spout");
		builder.setBolt("count", new CountBaseBasicBolt(), 1).fieldsGrouping("split", new Fields("word"));
		builder.setBolt("print", new PrintBaseBasicBolt(), 1).shuffleGrouping("count");

		Config conf = new Config();
		conf.setDebug(false);
		if (args != null && args.length > 0) {
			conf.setNumWorkers(3);
			StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
		} else {
			conf.setMaxTaskParallelism(1);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("wordcount", conf, builder.createTopology());
			Thread.sleep(60000);
			cluster.shutdown();
		}

	}

}
