/** 
 * Project Name:stormlearn 
 * File Name:TKafkaSource.java 
 * Package Name:stormlearn.trident.source.kafka 
 * Date:2017年8月10日下午4:46:30 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.source.kafka;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.kafka.trident.OpaqueTridentKafkaSpout;
import org.apache.storm.kafka.trident.TransactionalTridentKafkaSpout;
import org.apache.storm.kafka.trident.TridentKafkaConfig;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.Consumer;
import org.apache.storm.trident.tuple.TridentTuple;

/**
 * ClassName:TKafkaSource <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月10日 下午4:46:30 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings("unused")
public class TKafkaSource {
	@SuppressWarnings("serial")
	public static StormTopology getStormTopology() {
		TridentTopology topology = new TridentTopology();
		BrokerHosts zk = new ZkHosts("localhost:2181");
		TridentKafkaConfig spoutConfig = new TridentKafkaConfig(zk, "test", "TKafkaSource".toLowerCase());
		
		spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
		// OpaqueTridentKafkaSpout spout = new
		// OpaqueTridentKafkaSpout(spoutConfig);
		TransactionalTridentKafkaSpout spout = new TransactionalTridentKafkaSpout(spoutConfig);
		Stream streamSpout = topology.newStream("spout", spout).parallelismHint(1);

		streamSpout.peek(new Consumer() {
			@Override
			public void accept(TridentTuple input) {
				System.err.println("peek:" + input.getString(0));
			}
		});

		return topology.build();
	}

	public static void main(String[] args) throws Exception {
		Config conf = new Config();
		conf.setDebug(false);
		conf.setNumWorkers(10);
		conf.put("topology.spout.max.batch.size", 2);
		if (args != null && args.length > 0) {
			conf.setNumWorkers(5);
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
