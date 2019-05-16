/** 
 * Project Name:stormlearn 
 * File Name:WordCountTopology.java 
 * Package Name:stormlearn.component 
 * Date:2017年7月25日下午5:17:43 
 * sky.zyl@hotmail.com
*/

package stormlearn.component.irich;

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
public class WCTopoIRich {

	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("spout", new RandomIRichSpout(), 1);
		builder.setBolt("split", new SplitIRichBolt(), 1).shuffleGrouping("spout");
		builder.setBolt("count", new CountIRichBolt(), 1).fieldsGrouping("split", new Fields("word"));
		builder.setBolt("Printout", new PrintIRichBolt(), 1).shuffleGrouping("count");

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