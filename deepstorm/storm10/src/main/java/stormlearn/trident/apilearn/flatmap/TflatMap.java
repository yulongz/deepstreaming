/** 
 * Project Name:stormlearn 
 * File Name:TflatMap.java 
 * Package Name:stormlearn.trident.apilearn.flatmap 
 * Date:2017年8月5日下午11:06:50 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.flatmap;

import java.util.ArrayList;
import java.util.List;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.FlatMapFunction;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import stormlearn.trident.apilearn.each.AddSuffix;
import stormlearn.trident.apilearn.each.CopyFields;
import stormlearn.trident.apilearn.map.PrintFields;
import stormlearn.trident.apilearn.map.PrintFieldsName;
import stormlearn.trident.apilearn.map.ToLower;

/**
 * ClassName:TflatMap <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月5日 下午11:06:50 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings("unused")
public class TflatMap {

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
		topology.newStream("spout", spout).each(new Fields("sentence"), new CopyFields(), new Fields("sentencecopy"))
				.flatMap(split).map(new PrintFieldsName()).map(new PrintFields());

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
	private static FlatMapFunction split = new FlatMapFunction() {
		public Iterable<Values> execute(TridentTuple input) {
			List<Values> valuesList = new ArrayList<Values>();
			for (String word : input.getString(0).split(" ")) {
				valuesList.add(new Values(word, input.getString(1)));
			}
			return valuesList;
		}
	};

}
