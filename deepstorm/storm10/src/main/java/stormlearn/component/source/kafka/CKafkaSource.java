/** 
 * Project Name:stormlearn 
 * File Name:CKafkaSource.java 
 * Package Name:stormlearn.component.source.kafka 
 * Date:2017年8月10日下午4:51:24 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.component.source.kafka;

import java.util.ArrayList;
import java.util.List;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

/**
 * ClassName:CKafkaSource <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月10日 下午4:51:24 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class CKafkaSource {

	@SuppressWarnings({ "serial" })
	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();

		//offset config start
		BrokerHosts hosts = new ZkHosts("breath:2181");
		String topicName = "test";
		SpoutConfig spoutConfig = new SpoutConfig(hosts, "test", "/consumers/" + topicName,
				"CKafkaSource".toLowerCase());
		spoutConfig.zkPort = 2181;
		List<String> servers = new ArrayList<>();
		servers.add("breath");
		spoutConfig.zkServers = servers;
		//offset config start
		
		spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
		KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);

		builder.setSpout("spout", kafkaSpout);

		builder.setBolt("bolt", new BaseBasicBolt() {
			@Override
			public void execute(Tuple input, BasicOutputCollector collector) {
				System.err.println(input.getString(0));
			}

			@Override
			public void declareOutputFields(OutputFieldsDeclarer declarer) {
			}
		}).shuffleGrouping("spout");

		Config conf = new Config();
		conf.setDebug(false);
		if (args != null && args.length > 0) {
			conf.setNumWorkers(3);
			StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
		} else {
			conf.setMaxTaskParallelism(1);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("wordcount", conf, builder.createTopology());
			Thread.sleep(20000);
			cluster.shutdown();
		}
	}
}
