/** 
 * Project Name:kafkastreams 
 * File Name:ConsumerDemo.java 
 * Package Name:kafkastreams.consumer 
 * Date:2017年8月17日下午8:13:32 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package kafkastreams.consumer;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * ClassName:ConsumerDemo <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月17日 下午8:13:32 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class ConsumerDemo {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Properties props = new Properties(); 
		props.put("bootstrap.servers", "localhost:9092");// 所属Consumer Group的ID
		props.put("group.id", "test");// 所属Consumer Group的ID
		props.put("enable.auto.commit", "true");// 自动提交offset
		props.put("auto.commit.interval.ms", "1000");// 自动提交offset的时间间隔
		props.put("session.timeout.ms", "30000");
		// 指定key和value的序列化器
		props.put("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");


		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

		// 订阅topic
		consumer.subscribe(Arrays.asList("test"));
		try {
			while (true) {
				// 从服务端拉取消息，每次pool()可以拉取多个消息
				ConsumerRecords<String, String> records = consumer.poll(100);
				// 消费消息，这里仅仅是将消息的offset、key、value输出 
				for (ConsumerRecord<String, String> record : records) {
					System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(),
							record.value());
				}
			}
		} finally {

		}

	}

}
