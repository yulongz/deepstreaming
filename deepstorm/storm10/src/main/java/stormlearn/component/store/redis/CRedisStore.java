/** 
 * Project Name:stormlearn 
 * File Name:TRedisClusterStore.java 
 * Package Name:stormlearn.component.store.rediscluster 
 * Date:2017年8月10日上午10:29:06 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.component.store.redis;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.redis.bolt.RedisStoreBolt;
import org.apache.storm.redis.common.config.JedisPoolConfig;
import org.apache.storm.redis.common.mapper.RedisDataTypeDescription;
import org.apache.storm.redis.common.mapper.RedisStoreMapper;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.ITuple;
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
public class CRedisStore {

	@SuppressWarnings({})
	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("spout", new RandomBaseRichSpout(), 1);

		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig.Builder().setHost("localhost").setPort(6379).build();

		RedisStoreBolt bolt = new RedisStoreBolt(jedisPoolConfig, new RedisStoreMapperImp());

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
			return new RedisDataTypeDescription(RedisDataTypeDescription.RedisDataType.HASH, "stormredis");
		}
	}
}
