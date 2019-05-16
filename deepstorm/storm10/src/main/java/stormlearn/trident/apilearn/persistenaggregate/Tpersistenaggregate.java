/** 
 * Project Name:stormlearn 
 * File Name:Tpersistenaggregate.java 
 * Package Name:stormlearn.trident.apilearn.persistenaggregate 
 * Date:2017年8月9日下午3:52:44 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.persistenaggregate;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.LocalDRPC;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentState;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.testing.MemoryMapState;
import org.apache.storm.tuple.Fields;

import stormlearn.trident.apilearn.aggregate.CountReduceAggregator;
import stormlearn.trident.apilearn.map.PrintFields;

/**
 * ClassName:Tpersistenaggregate <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月9日 下午3:52:44 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class Tpersistenaggregate {
	@SuppressWarnings("unused")
	public static StormTopology getStormTopology(LocalDRPC drpc) {
		// declare TridentTopology
		TridentTopology topology = new TridentTopology();
		Stream streamSpout = topology.newStream("spout", new RandomBaseRichSpout()).parallelismHint(1);
		TridentState streamState = streamSpout.groupBy(new Fields("sentence")).persistentAggregate(
				new MemoryMapState.Factory(), new Fields("sentence"), new CountReduceAggregator(), new Fields("count"));

//		topology.newDRPCStream("words", drpc).groupBy(new Fields("count"))
//				.stateQuery(streamState, new Fields("sentence"), new MapGet(), new Fields("count"))
//				.map(new PrintFields(true));
		topology.newDRPCStream("words", drpc).map(new PrintFields());

		return topology.build();
	}

	public static void main(String[] args) throws Exception {
		Config conf = new Config();
		conf.setDebug(false);
		conf.setNumWorkers(10);
		conf.put("topology.spout.max.batch.size", 2);
		if (args != null && args.length > 0) {
			conf.setNumWorkers(10);
			StormSubmitter.submitTopology(args[0], conf, getStormTopology(null));
		} else {
			conf.setMaxTaskParallelism(1);
			LocalCluster cluster = new LocalCluster();
			LocalDRPC drpc = new LocalDRPC();
			cluster.submitTopology("TridentApi", conf, getStormTopology(drpc));
			Thread.sleep(20000);
			cluster.shutdown();
		}
	}
}
