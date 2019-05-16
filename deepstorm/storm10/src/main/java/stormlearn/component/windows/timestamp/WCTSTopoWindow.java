/** 
 * Project Name:stormlearn 
 * File Name:WCTopoBaseBasic.java 
 * Package Name:stormlearn.component.basebasic 
 * Date:2017年8月2日下午4:28:36 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.component.windows.timestamp;

import java.util.concurrent.TimeUnit;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
//import org.apache.storm.topology.base.BaseWindowedBolt.Count;
import org.apache.storm.topology.base.BaseWindowedBolt.Duration;

/**
 * ClassName:WCTopoBaseBasic <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月2日 下午4:28:36 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class WCTSTopoWindow {

	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("spout", new RandomBaseRichSpout(), 1);
		//// SlidingWindow
		builder.setBolt("print",
				new PrintSlidingWindowedBolt()
						.withWindow(new Duration(10000, TimeUnit.MILLISECONDS),
								new Duration(5000, TimeUnit.MILLISECONDS))
						.withTimestampField("ts").withLag(new Duration(5000, TimeUnit.MILLISECONDS))
						.withWatermarkInterval(new Duration(1000, TimeUnit.MILLISECONDS)),
				1).allGrouping("spout");

		Config conf = new Config();
		conf.setDebug(false);
		if (args != null && args.length > 0) {
			conf.setNumWorkers(3);
			StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
		} else {
			conf.setMaxTaskParallelism(1);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology(WCTSTopoWindow.class.getSimpleName(), conf, builder.createTopology());
			Thread.sleep(60000);
			cluster.shutdown();
		}

	}

}
