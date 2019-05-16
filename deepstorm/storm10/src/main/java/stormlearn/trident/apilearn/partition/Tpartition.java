/** 
 * Project Name:stormlearn 
 * File Name:Tpartition.java 
 * Package Name:stormlearn.trident.apilearn.partition 
 * Date:2017年8月6日下午2:11:38 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.partition;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.MapFunction;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import stormlearn.trident.apilearn.map.PrintFields;

/**
 * ClassName:Tpartition <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月6日 下午2:11:38 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings({ "unused" })
public class Tpartition {
	public static StormTopology getStormTopology() {
		TridentTopology topology = new TridentTopology();
		Stream streamSpout = topology.newStream("spout", new RandomBaseRichSpout()).parallelismHint(2);
		Stream streambolt = streamSpout.partition(new ModStreamGrouping(new Fields("sentence"),true)).map(new PrintFields(true))
				.parallelismHint(3);
		// partition(new ModStreamGrouping()).
		return topology.build();
	}

	public static void main(String[] args) throws Exception {
		Config conf = new Config();
		conf.setDebug(false);
		conf.setNumWorkers(10);

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
