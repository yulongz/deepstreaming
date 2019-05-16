/** 
 * Project Name:stormlearn 
 * File Name:Tbroadcast.java 
 * Package Name:stormlearn.trident.apilearn.partition 
 * Date:2017年8月6日下午2:16:01 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/  
  
package stormlearn.trident.apilearn.partition;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;

import stormlearn.trident.apilearn.map.NothingMap;
import stormlearn.trident.apilearn.map.PrintFields;

/** 
 * ClassName:Tbroadcast <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年8月6日 下午2:16:01 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
@SuppressWarnings("unused")
public class TidentityPartition {

	public static StormTopology getStormTopology() {
		//多个分区操作时，identityPartition负责将前面的partition操作并发度增大至identityPartition并发度，如果小于前面的则不操作。
		//举例：shuffle1 with parallel(1) -> shuffle2 with parallel(3) -> identityPartition with parallel(2),则shuffle1的parallel增至2,shuffle2不变。
		TridentTopology topology = new TridentTopology();
		Stream streamSpout = topology.newStream("spout", new RandomBaseRichSpout(true)).parallelismHint(1);
		Stream streambolt1 = streamSpout.shuffle().map(new NothingMap(true)).parallelismHint(1);
		Stream streambolt2 = streambolt1.shuffle().map(new NothingMap(true)).parallelismHint(3);
		Stream streambolt3 = streambolt2.identityPartition().map(new PrintFields(true)).parallelismHint(2);
		
		//identityPartition与spout之间不存在partition操作时，可提升spout的并行度。
		//举例：spout with parallel(1) -> identityPartition with parallel(2),则spout的parallel增至2。
		// Stream streamSpout = topology.newStream("spout", new RandomBaseRichSpout(true)).parallelismHint(1);
		// Stream streambolt = streamSpout.identityPartition().map(new PrintFields(true)).parallelismHint(2);

		return topology.build();
	}

	public static void main(String[] args) throws Exception {
		Config conf = new Config();
		conf.setDebug(false);
		conf.setNumWorkers(10);

		if (args != null && args.length > 0) {
			conf.setNumWorkers(10);
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
 