/** 
 * Project Name:stormlearn 
 * File Name:WordCountTopology.java 
 * Package Name:stormlearn.component 
 * Date:2017年7月25日下午5:17:43 
 * sky.zyl@hotmail.com
*/

package stormlearn.component.baserich;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

/**
 * ClassName:WordCountTopology <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月25日 下午5:17:43 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class WCTopoBaseRich {

	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("spout", new RandomBaseRichSpout(), 1);
		builder.setBolt("split", new SplitBaseRichBolt(), 1).shuffleGrouping("spout");
		builder.setBolt("count", new CountBaseRichBolt(), 1).fieldsGrouping("split", new Fields("word"));
		builder.setBolt("print", new PrintBaseRichBolt(), 1).shuffleGrouping("count");

		Config conf = new Config();
		conf.setDebug(false);
		if (args != null && args.length > 0) {
			conf.setNumWorkers(3);
			StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
		} else {
			conf.setMaxTaskParallelism(1);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology(WCTopoBaseRich.class.getSimpleName(), conf, builder.createTopology());
			Thread.sleep(60000);
			cluster.shutdown();
		}
	}
}