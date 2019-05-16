/** 
 * Project Name:stormlearn 
 * File Name:Tfilter.java 
 * Package Name:stormlearn.trident.apilearn.filter 
 * Date:2017年8月5日下午8:53:40 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.filter;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import stormlearn.trident.apilearn.each.PrintFields;

/**
 * ClassName:Tfilter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月5日 下午8:53:40 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class Tfilter {

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
		topology.newStream("spout", spout).filter(new Fields("sentence"), new MyFilter()).each(new Fields("sentence"),
				new PrintFields(true), new Fields("end"));

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
