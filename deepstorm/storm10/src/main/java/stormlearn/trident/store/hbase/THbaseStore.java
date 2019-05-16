/** 
 * Project Name:stormlearn 
 * File Name:THbaseStore.java 
 * Package Name:stormlearn.trident.store.hbase 
 * Date:2017年8月10日下午3:26:55 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.store.hbase;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.hbase.client.Durability;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.hbase.trident.mapper.SimpleTridentHBaseMapper;
import org.apache.storm.hbase.trident.mapper.TridentHBaseMapper;
import org.apache.storm.hbase.trident.state.HBaseState;
import org.apache.storm.hbase.trident.state.HBaseStateFactory;
import org.apache.storm.hbase.trident.state.HBaseUpdater;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.state.StateFactory;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

/**
 * ClassName:THbaseStore <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月10日 下午3:26:55 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class THbaseStore {
	@SuppressWarnings("unchecked")
	public static StormTopology getStormTopology() {

		FixedBatchSpout spout = new FixedBatchSpout(new Fields("word", "count"), 4, new Values("storm", 1),
				new Values("trident", 1), new Values("needs", 1), new Values("javadoc", 1));
		spout.setCycle(false);

		TridentTopology topology = new TridentTopology();
		Stream streamSpout = topology.newStream("spout", spout).parallelismHint(1);

		TridentHBaseMapper tridentHBaseMapper = new SimpleTridentHBaseMapper().withColumnFamily("cf")
				.withColumnFields(new Fields("word")).withCounterFields(new Fields("count")).withRowKeyField("word");

		HBaseState.Options options = new HBaseState.Options().withConfigKey("hbase.conf")
				.withDurability(Durability.SKIP_WAL).withMapper(tridentHBaseMapper)
				.withTableName("stormwc");

		StateFactory stateFactory = new HBaseStateFactory(options);

		streamSpout.partitionPersist(stateFactory, streamSpout.getOutputFields(), new HBaseUpdater(), new Fields())
				.parallelismHint(1);

		return topology.build();
	}

	public static void main(String[] args) throws Exception {
		Config conf = new Config();
		conf.setDebug(false);
		conf.setNumWorkers(10);
		conf.put("topology.spout.max.batch.size", 2);
		Map<String, Object> hbConf = new HashMap<String, Object>();
		if (args != null && args.length > 0) {
			conf.setNumWorkers(10);
			hbConf.put("hbase.rootdir", args[1]);
			hbConf.put("zookeeper.znode.parent", args[2]);
			hbConf.put("hbase.zookeeper.quorum", args[3]);
			conf.put("hbase.conf", hbConf);
			StormSubmitter.submitTopology(args[0], conf, getStormTopology());
		} else {
			conf.setMaxTaskParallelism(1);
			hbConf.put("hbase.rootdir", "/hbase");
			hbConf.put("zookeeper.znode.parent", "/hbase");
			hbConf.put("hbase.zookeeper.quorum", "breath");
			conf.put("hbase.conf", hbConf);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("TridentApi", conf, getStormTopology());
			Thread.sleep(20000);
			cluster.shutdown();
		}
	}
}
