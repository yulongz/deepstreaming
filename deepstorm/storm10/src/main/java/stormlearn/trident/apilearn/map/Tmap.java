/** 
 * Project Name:stormlearn 
 * File Name:Tmap.java 
 * Package Name:stormlearn.trident.apilearn.map 
 * Date:2017年8月5日下午9:40:03 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.map;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.MapFunction;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.trident.tuple.TridentTuple;
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
public class Tmap {

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
		topology.newStream("spout", spout).map(toUpper).map(new PrintFields());

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

	@SuppressWarnings("serial")
	private static MapFunction toUpper = new MapFunction() {
		public Values execute(TridentTuple input) {
			return new Values(input.getStringByField("sentence").toUpperCase());
		}
	};

}
