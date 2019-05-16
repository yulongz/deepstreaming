/** 
 * Project Name:stormlearn 
 * File Name:Tmap.java 
 * Package Name:stormlearn.trident.apilearn.map 
 * Date:2017年8月5日下午9:40:03 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.peek;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.Consumer;
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
@SuppressWarnings("serial")
public class Tpeek {

	@SuppressWarnings("unused")
	public static StormTopology getStormTopology() {
		// create spout
		@SuppressWarnings("unchecked")
		FixedBatchSpout spout = new FixedBatchSpout(new Fields("sentence"), 1,
				new Values("the cow jumped over the moon"),
				new Values("the man went to the store and bought some candy"),
				new Values("four score and seven years ago"), new Values("how many apples can you eat"));
		spout.setCycle(false);

		// declare TridentTopology
		TridentTopology topology = new TridentTopology();
		Stream streamspout = topology.newStream("spout", spout);

		// peek1
		Stream streampeek1 = streamspout.peek(new Consumer() {
			@Override
			public void accept(TridentTuple input) {
				System.err.println("peek1:" + input.getString(0));
			}
		});
		// peek2 use class implements
		Stream streampeek2 = streamspout.peek(new LocalPeekConsumer());
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

@SuppressWarnings("serial")
class LocalPeekConsumer implements Consumer {
	@Override
	public void accept(TridentTuple input) {
		System.err.println("peek2:" + input.getString(0));
	}
}
