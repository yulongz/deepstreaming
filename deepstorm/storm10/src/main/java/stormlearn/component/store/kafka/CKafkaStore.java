/** 
 * Project Name:stormlearn 
 * File Name:TpartitionPersist.java 
 * Package Name:stormlearn.trident.apilearn.partitionpersist 
 * Date:2017年8月9日下午4:20:10 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.component.store.kafka;

import java.util.Properties;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.bolt.KafkaBolt;
import org.apache.storm.kafka.bolt.mapper.FieldNameBasedTupleToKafkaMapper;
import org.apache.storm.kafka.bolt.selector.DefaultTopicSelector;
import org.apache.storm.topology.TopologyBuilder;

import stormlearn.component.store.RandomBaseRichSpout;

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
public class CKafkaStore {

	@SuppressWarnings({ })
	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("spout", new RandomBaseRichSpout(), 1);
		// set producer properties.
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "1");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		KafkaBolt<String, String> bolt = new KafkaBolt<String, String>().withProducerProperties(props)
				.withTopicSelector(new DefaultTopicSelector("test"))
				.withTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper<String, String>("key", "value"));
		
		builder.setBolt("forwardToKafka", bolt, 1).shuffleGrouping("spout");

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
