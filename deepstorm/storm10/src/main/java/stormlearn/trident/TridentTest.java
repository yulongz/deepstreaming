/** 
 * Project Name:stormlearn 
 * File Name:TridentTest.java 
 * Package Name:stormlearn.trident 
 * Date:2017年7月26日下午4:57:38 
 * sky.zyl@hotmail.com
*/

package stormlearn.trident;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.operation.builtin.Count;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.trident.testing.Split;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

/**
 * ClassName:TridentTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月26日 下午4:57:38 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class TridentTest {

	public static void main(String[] args) throws Exception {
		/* 创建spout */
		@SuppressWarnings("unchecked")
		FixedBatchSpout spout = new FixedBatchSpout(new Fields("sentence"), 3,
				new Values("the cow jumped over the moon"),
				new Values("the man went to the store and bought some candy"),
				new Values("four score and seven years ago"), new Values("how many apples can you eat"));
		spout.setCycle(true);

		/* 创建Tridenttopology */
		TridentTopology topology = new TridentTopology();

		topology.newStream("spout2", spout).each(new Fields("sentence"), new Split(), new Fields("word"))
				.groupBy(new Fields("word")).aggregate(new Count(), new Fields("count"))
				.each(new Fields("word", "count"), new PrintUtil(), new Fields("word1", "count1")).parallelismHint(1);

		Config conf = new Config();
		conf.setDebug(false);
		conf.setNumWorkers(3);
		LocalCluster localcluster = new LocalCluster();
		localcluster.submitTopology("WordCount", conf, topology.build());
		Thread.sleep(60000);
		localcluster.shutdown();
	}
}

@SuppressWarnings("serial")
class PrintUtil extends BaseFunction {
	private String word;
	private Long count;
	public void execute(TridentTuple tuple, TridentCollector collector) {
		word = tuple.getString(0);
		count = tuple.getLong(1);
		System.out.println(word + ":" + count);
		collector.emit(new Values(word, count));
	}
}
