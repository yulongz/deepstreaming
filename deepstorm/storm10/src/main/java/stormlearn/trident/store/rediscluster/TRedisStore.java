/** 
 * Project Name:stormlearn 
 * File Name:TpartitionPersist.java 
 * Package Name:stormlearn.trident.apilearn.partitionpersist 
 * Date:2017年8月9日下午4:20:10 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.store.rediscluster;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.redis.common.config.JedisPoolConfig;
import org.apache.storm.redis.common.mapper.RedisDataTypeDescription;
import org.apache.storm.redis.common.mapper.RedisStoreMapper;
import org.apache.storm.redis.trident.state.RedisState;
import org.apache.storm.redis.trident.state.RedisState.Factory;
import org.apache.storm.redis.trident.state.RedisStateUpdater;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.ITuple;
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
public class TRedisStore {
	@SuppressWarnings("unchecked")
	public static StormTopology getStormTopology() {

		FixedBatchSpout spout = new FixedBatchSpout(new Fields("value", "key"), 1,
				new Values("the cow jumped over the moon", "0"),
				new Values("the man went to the store and bought some candy", "1"),
				new Values("four score and seven years ago", "2"), new Values("how many apples can you eat", "3"));
		spout.setCycle(false);

		TridentTopology topology = new TridentTopology();
		Stream streamSpout = topology.newStream("spout", spout).parallelismHint(1);

		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig.Builder().setHost("localhost").setPort(6379).build();
		Factory stateFactory = new RedisState.Factory(jedisPoolConfig);
		streamSpout.partitionPersist(stateFactory, streamSpout.getOutputFields(),
				new RedisStateUpdater(new RedisStoreMapperImp()), new Fields()).parallelismHint(1);

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

	@SuppressWarnings("serial")
	static class RedisStoreMapperImp implements RedisStoreMapper {
		@Override
		public String getKeyFromTuple(ITuple tuple) {
			return tuple.getStringByField("key");
		}

		@Override
		public String getValueFromTuple(ITuple tuple) {
			return tuple.getStringByField("value");
		}

		@Override
		public RedisDataTypeDescription getDataTypeDescription() {
			return new RedisDataTypeDescription(RedisDataTypeDescription.RedisDataType.HASH, "tridentredis");
		}
	}
}
