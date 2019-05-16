/** 
 * Project Name:stormlearn 
 * File Name:Taggrate.java 
 * Package Name:stormlearn.trident.apilearn.aggrate 
 * Date:2017年8月7日下午5:22:58 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.aggregate;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.Consumer;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Fields;

import stormlearn.trident.apilearn.map.PrintFields;
import stormlearn.trident.apilearn.map.PrintFieldsName;
import stormlearn.trident.apilearn.peek.PeekConsumer;

/**
 * ClassName:Taggregate <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月7日 下午5:22:58 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings({ "unused" })
public class Taggregate {
	public static StormTopology getStormTopology() {
		// declare TridentTopology
		TridentTopology topology = new TridentTopology();
		Stream streamSpout = topology.newStream("spout", new RandomBaseRichSpout()).parallelismHint(1);
		Stream streambolt = streamSpout
				// .aggregate(new Fields("sentence"), new
				// CountCombinerAggregator(), new Fields("count"))
				.aggregate(new Fields("sentence"), new CountReduceAggregator(), new Fields("count"))
				.map(new PrintFields(true)).parallelismHint(2);

		return topology.build();
	}

	public static void main(String[] args) throws Exception {
		Config conf = new Config();
		conf.setDebug(false);
		conf.setNumWorkers(10);
		conf.put("topology.spout.max.batch.size", 2);
		if (args != null && args.length > 0) {
			conf.setNumWorkers(10);
			StormSubmitter.submitTopology(args[0], conf, getStormTopology());
		} else {
			conf.setMaxTaskParallelism(1);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("TridentApi", conf, getStormTopology());
			Thread.sleep(20000);
			cluster.shutdown();
		}
	}
}
