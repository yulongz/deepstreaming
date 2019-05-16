/** 
 * Project Name:stormlearn 
 * File Name:TchainedAgg.java 
 * Package Name:stormlearn.trident.apilearn.chainedAgg 
 * Date:2017年8月8日下午3:36:37 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.chainedagg;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.tuple.Fields;

import stormlearn.trident.apilearn.aggregate.CountReduceAggregator;
import stormlearn.trident.apilearn.aggregate.RandomBaseRichSpout;
import stormlearn.trident.apilearn.aggregate.SumReduceAggregator;
import stormlearn.trident.apilearn.map.PrintFields;

/**
 * ClassName:TchainedAgg <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月8日 下午3:36:37 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings("unused")
public class TchainedAgg {

	public static StormTopology getStormTopology() {
		// declare TridentTopology
		TridentTopology topology = new TridentTopology();
		Stream streamSpout = topology.newStream("spout", new RandomBaseRichSpout()).parallelismHint(1);
		Stream streambolt = streamSpout
				.chainedAgg()
				.aggregate(new Fields("sentence"), new CountReduceAggregator(), new Fields("count"))
				.aggregate(new Fields("sentence"), new SumReduceAggregator(), new Fields("sum"))
				.chainEnd()
				.map(new PrintFields(true));
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
