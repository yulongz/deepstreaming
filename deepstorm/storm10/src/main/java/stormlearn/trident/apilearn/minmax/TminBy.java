/** 
 * Project Name:stormlearn 
 * File Name:Tmap.java 
 * Package Name:stormlearn.trident.apilearn.map 
 * Date:2017年8月5日下午9:40:03 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.minmax;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.builtin.Debug;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

/**
 * ClassName:Tmap <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月5日 下午9:40:03 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class TminBy {

	public static StormTopology getStormTopology() {
		@SuppressWarnings("unchecked")
		FixedBatchSpout spout = new FixedBatchSpout(new Fields("sentence"), 4, new Values(0), new Values(1),
				new Values(2), new Values(3));
		// must set batch size > 1
		spout.setCycle(true);

		TridentTopology topology = new TridentTopology();
		topology.newStream("spout", spout).minBy("sentence").each(new Fields("sentence"), new Debug());

		return topology.build();
	}

	public static void main(String[] args) throws Exception {
		Config conf = new Config();
		conf.setDebug(false);
		conf.setNumWorkers(4);
		LocalCluster localcluster = new LocalCluster();
		localcluster.submitTopology("TridentApi", conf, getStormTopology());
		Thread.sleep(20000);
		localcluster.shutdown();
	}
}
