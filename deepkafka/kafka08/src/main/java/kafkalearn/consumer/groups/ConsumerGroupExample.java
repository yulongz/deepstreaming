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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

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
public class ConsumerGroupExample {
	private final ConsumerConnector consumer;
	private final String topic;
	private ExecutorService executor;
	

	public ConsumerGroupExample(String a_zookeeper, String a_groupId, String a_topic) {
		consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig(a_zookeeper, a_groupId));
		this.topic = a_topic;
	}

	public void shutdown() {
		if (consumer != null)
			consumer.shutdown();
		if (executor != null)
			executor.shutdown();
		try {
			if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
				System.out.println("Timed out waiting for consumer threads to shut down, exiting uncleanly");
			}
		} catch (InterruptedException e) {
			System.out.println("Interrupted during shutdown, exiting uncleanly");
		}
	}

	@SuppressWarnings("rawtypes")
	public void run(int a_numThreads) {
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		// 决定一个topic启动几个线程去拉取数据，即一个topic生成几个KafkaStream；
		topicCountMap.put(topic, new Integer(a_numThreads));
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
		List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

		// now launch all the threads
		//
		executor = Executors.newFixedThreadPool(a_numThreads);

		// now create an object to consume the messages
		//
		int threadNumber = 0;
		System.out.println(streams.size());
		for (KafkaStream stream : streams) {
			executor.submit(new ConsumerTest(stream, threadNumber));
			System.out.println("submit");
			threadNumber++;
		}
	}

	private ConsumerConfig createConsumerConfig(String a_zookeeper, String a_groupId) {
		Properties props = new Properties();
		props.put("zookeeper.connect", a_zookeeper);
		props.put("group.id", a_groupId);
		props.put("zookeeper.session.timeout.ms", "400");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");
		props.put("auto.offset.reset", "smallest");//必须要加，如果要读旧数据  
		return new ConsumerConfig(props);
	}

	public static void main(String[] args) {
		// String zooKeeper = args[0];
		// String groupId = args[1];
		// String topic = args[2];
		// int threads = Integer.parseInt(args[3]);
		//String zooKeeper = "172.16.13.151:2181,172.16.13.152:2181,172.16.13.153:2181/kafka";
		String zooKeeper = "breath:2181";
		String groupId = "kafkahighlevelapi";
		String topic = "test";
		int threads = Integer.parseInt("1");
		ConsumerGroupExample example = new ConsumerGroupExample(zooKeeper, groupId, topic);
		example.run(threads);

		try {
			Thread.sleep(100000);
		} catch (InterruptedException ie) {

		}
		example.shutdown();
	}

}
