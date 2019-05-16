/** 
 * Project Name:stormlearn 
 * File Name:TpartitionPersist.java 
 * Package Name:stormlearn.trident.apilearn.partitionpersist 
 * Date:2017年8月9日下午4:20:10 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.partitionpersist;

import java.util.Properties;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.trident.TridentKafkaStateFactory;
import org.apache.storm.kafka.trident.TridentKafkaUpdater;
import org.apache.storm.kafka.trident.mapper.FieldNameBasedTupleToKafkaMapper;
import org.apache.storm.kafka.trident.selector.DefaultTopicSelector;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;



/**
 * ClassName:TpartitionPersist <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月9日 下午4:20:10 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class TpartitionPersist {
	@SuppressWarnings("unchecked")
	public static StormTopology getStormTopology() {

		FixedBatchSpout spout = new FixedBatchSpout(new Fields("sentence","id"), 1,
				new Values("the cow jumped over the moon","0"), new Values("the man went to the store and bought some candy","1"),
				new Values("four score and seven years ago","2"), new Values("how many apples can you eat","3"));
		spout.setCycle(false);

		TridentTopology topology = new TridentTopology();
		Stream streamSpout = topology.newStream("spout", spout).parallelismHint(1);

		Properties props = new Properties();
		props.put("bootstrap.servers", "breath:9092");
		props.put("acks", "1");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		//props.put("partitioner.class", "ModParititoner");

		//TridentKafkaStateFactory
		TridentKafkaStateFactory stateFactory = new TridentKafkaStateFactory().withProducerProperties(props)
				.withKafkaTopicSelector(new DefaultTopicSelector("test")).withTridentTupleToKafkaMapper(
						new FieldNameBasedTupleToKafkaMapper<String, String>("id", "sentence"));
		
		//partitionPersist
		streamSpout.partitionPersist(stateFactory, streamSpout.getOutputFields(), new TridentKafkaUpdater(),
				new Fields()).parallelismHint(1);

		return topology.build();
	}

	public static void main(String[] args) throws Exception {
		Config conf = new Config();
		conf.setDebug(false);
		conf.setNumWorkers(10);
		conf.put("topology.spout.max.batch.size", 2);
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
