/** 
 * Project Name:kafkalearn 
 * File Name:ConsumerGroupExample.java 
 * Package Name:com.test.groups 
 * Date:2017年8月16日下午12:02:37 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package kafkalearn.consumer.groups;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

/**
 * ClassName:ConsumerGroupExample <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月16日 下午12:02:37 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class ConsumerGroupExampleDemo {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		// String zooKeeper = args[0];
		// String groupId = args[1];
		// String topic = args[2];
		// int threads = Integer.parseInt(args[3]);
		String zooKeeper = "breath:2181";
		String groupId = "kafkahighlevelapidemo";
		String topic = "test";
		int threads = Integer.parseInt("1");

		// ConsumerConfig
		Properties props = new Properties();
		props.put("zookeeper.connect", zooKeeper);
		props.put("group.id", groupId);
		props.put("zookeeper.session.timeout.ms", "400");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");
		props.put("auto.offset.reset", "smallest");

		ConsumerConfig consumerConfig = new ConsumerConfig(props);
		ConsumerConnector consumer = kafka.consumer.Consumer.createJavaConsumerConnector(consumerConfig);

		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, new Integer(threads));
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
		List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

		for (final KafkaStream stream : streams) {
			System.out.println("Start Thread: " + 0);
			ConsumerIterator<byte[], byte[]> it = stream.iterator();
			
			// System.out.println(it);
			MessageAndMetadata<byte[], byte[]> next;
			while (it.hasNext()) {
				next = it.next();
				System.out.println("Thread " + 0 + ": " + new String(next.message()));
			}
			System.out.println("Shutting down Thread: " + 0);
		}

		try {
			Thread.sleep(10000);
		} catch (InterruptedException ie) {

		}
	}

}
