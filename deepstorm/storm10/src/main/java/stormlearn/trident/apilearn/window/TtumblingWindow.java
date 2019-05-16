/** 
 * Project Name:storm2test 
 * File Name:TtumblingWindow.java 
 * Package Name:stormlearn.trident.apilearn.window 
 * Date:2017年8月8日下午4:06:34 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.window;

import java.util.concurrent.TimeUnit;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.builtin.Debug;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.trident.windowing.InMemoryWindowsStoreFactory;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import stormlearn.trident.apilearn.aggregate.CountAggregator;

/**
 * ClassName:TtumblingWindow <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月8日 下午4:06:34 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class TtumblingWindow {

	@SuppressWarnings("unused")
	public static StormTopology getStormTopology() {
		// create spout
		@SuppressWarnings("unchecked")
		FixedBatchSpout spout = new FixedBatchSpout(new Fields("sentence"), 1,
				new Values("the cow jumped over the moon"),
				new Values("the man went to the store and bought some candy"),
				new Values("four score and seven years ago"), new Values("how many apples can you eat"));
		spout.setCycle(true);

		// declare TridentTopology
		TridentTopology topology = new TridentTopology();
		Stream streamspout = topology.newStream("spout", spout);

		// streambolt
		Stream streambolt = streamspout.tumblingWindow(new BaseWindowedBolt.Duration(5000, TimeUnit.MILLISECONDS),
				new InMemoryWindowsStoreFactory(), 
				new Fields("sentence"), 
				new CountAggregator(), 
				new Fields("count"))
				.each(new Fields("count"), new Debug());
		return topology.build();
	}

	public static void main(String[] args) throws Exception {
		Config conf = new Config();
		conf.setDebug(false);
		conf.setNumWorkers(4);
		LocalCluster localcluster = new LocalCluster();
		localcluster.submitTopology("TridentApi", conf, getStormTopology());
		Thread.sleep(30000);
		localcluster.shutdown();
	}

}
