/** 
 * Project Name:stormlearn 
 * File Name:TRedisClusterStore.java 
 * Package Name:stormlearn.component.store.rediscluster 
 * Date:2017年8月10日上午10:29:06 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.component.store.hbase;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.hbase.bolt.HBaseBolt;
import org.apache.storm.hbase.bolt.mapper.SimpleHBaseMapper;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import stormlearn.component.store.RandomBaseRichSpout;

/**
 * ClassName:TRedisClusterStore <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月10日 上午10:29:06 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class CHbaseStore {

	@SuppressWarnings({})
	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();
		Map<String, Object> hbConf = new HashMap<String, Object>();
		if (args.length > 0) {
			hbConf.put("hbase.rootdir", args[1]);
			hbConf.put("zookeeper.znode.parent", args[2]);
			hbConf.put("hbase.zookeeper.quorum", args[3]);
		} else {
			hbConf.put("hbase.rootdir", "/hbase");
			hbConf.put("zookeeper.znode.parent", "/hbase");
			hbConf.put("hbase.zookeeper.quorum", "breath");
		}

		SimpleHBaseMapper mapper = new SimpleHBaseMapper()
				.withRowKeyField("word")
				.withColumnFields(new Fields("word"))
				//.withColumnFields(new Fields("word","count"))//columns name need to keep with fields name
				.withCounterFields(new Fields("count"))
				.withColumnFamily("cf");

		String Htable = "stormwc";
		HBaseBolt hbasebolt = new HBaseBolt(Htable, mapper).withConfigKey("hbase.conf");

		builder.setSpout("spout", new RandomBaseRichSpout(), 1);
		builder.setBolt("wordcounter", new WordCounter(), 1).shuffleGrouping("spout");
		builder.setBolt("forwardtohbase", hbasebolt, 1).fieldsGrouping("wordcounter", new Fields("word"));

		Config conf = new Config();
		conf.put("hbase.conf", hbConf);
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

	@SuppressWarnings("serial")
	static class WordCounter extends BaseBasicBolt {

		@Override
		public void execute(Tuple input, BasicOutputCollector collector) {
			collector.emit(new Values(input.getStringByField("key"), 1));
		}

		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			declarer.declare(new Fields("word", "count"));
		}
	}

}
