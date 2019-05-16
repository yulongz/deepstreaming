/** 
 * Project Name:stormlearn 
 * File Name:TridentWordCount.java 
 * Package Name:stormlearn.trident 
 * Date:2017年7月25日下午5:43:09 
 * sky.zyl@hotmail.com
*/

package stormlearn.trident.drpc;

import org.apache.storm.Config;
import org.apache.storm.LocalDRPC;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.thrift.TException;
import org.apache.storm.trident.TridentState;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.builtin.Count;
import org.apache.storm.trident.operation.builtin.FilterNull;
import org.apache.storm.trident.operation.builtin.MapGet;
import org.apache.storm.trident.operation.builtin.Sum;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.trident.testing.MemoryMapState;
import org.apache.storm.trident.testing.Split;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassName:TridentWordCount <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月25日 下午5:43:09 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class TridentWordCount {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(TridentWordCount.class);

	@SuppressWarnings("unchecked")
	private static StormTopology buildTopology(LocalDRPC drpc) {
		/* 创建spout */
		FixedBatchSpout spout = new FixedBatchSpout(new Fields("sentence"), 3,
				new Values("the cow jumped over the moon"),
				new Values("the man went to the store and bought some candy"),
				new Values("four score and seven years ago"), new Values("how many apples can you eat"));
		spout.setCycle(true);

		/* 创建Tridenttopology */
		TridentTopology topology = new TridentTopology();

		/* 创建Stream spout1, 分词、统计 */
		TridentState wordCounts = topology.newStream("spout1", spout)
				.each(new Fields("sentence"), new Split(), new Fields("word")).groupBy(new Fields("word"))
				.persistentAggregate(new MemoryMapState.Factory(), new Count(), new Fields("count")).parallelismHint(1);

		/* 创建Stream words，方法名为words，对入参分次，分别获取words 对应count，然后计算和 */
		topology.newDRPCStream("words").each(new Fields("args"), new Split(), new Fields("word"))
				.groupBy(new Fields("word"))
				.stateQuery(wordCounts, new Fields("word"), new MapGet(), new Fields("count"))
				.each(new Fields("count"), new FilterNull())
				.aggregate(new Fields("count"), new Sum(), new Fields("sum"));

		return topology.build();
	}

	public static void main(String[] args) throws InterruptedException {

		Config conf = new Config();
		conf.setDebug(false);
		conf.setMaxSpoutPending(20);

		try {
			conf.setNumWorkers(3);
			StormSubmitter.submitTopologyWithProgressBar("WordCount", conf, buildTopology(null));
		} catch (AlreadyAliveException e) {
			e.printStackTrace();
		} catch (InvalidTopologyException e) {
			e.printStackTrace();
		} catch (TException e) {
			e.printStackTrace();
		} finally {
		}
	}

}
