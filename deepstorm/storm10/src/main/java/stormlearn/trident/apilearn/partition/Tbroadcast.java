/** 
 * Project Name:stormlearn 
 * File Name:Tbroadcast.java 
 * Package Name:stormlearn.trident.apilearn.partition 
 * Date:2017年8月6日下午2:16:01 
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

import stormlearn.trident.apilearn.map.PrintFields;

/** 
 * ClassName:Tbroadcast <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年8月6日 下午2:16:01 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
@SuppressWarnings("unused")
public class Tbroadcast {

	public static StormTopology getStormTopology() {
		// declare TridentTopology
		TridentTopology topology = new TridentTopology();
		Stream streamSpout = topology.newStream("spout", new RandomBaseRichSpout()).parallelismHint(2);
		Stream streambolt = streamSpout.broadcast().map(new PrintFields(true)).parallelismHint(3);

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
 