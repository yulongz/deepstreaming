/** 
 * Project Name:kafkastreams 
 * File Name:KafkaOffsetModify.java 
 * Package Name:kafkastreams 
 * Date:2017年8月18日下午3:37:55 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package kafkastreams.util;

/** 
 * ClassName:KafkaOffsetModify <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年8月18日 下午3:37:55 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import kafka.message.Message;

//import cn.xxx.kafka.Message;

public class KafkaOffsetModify {

	private static String seek(String[] args) {
		String bootstrapservers = args[2];
		String applicationId = args[3];
		String topic = args[4];
		int partition = Integer.parseInt(args[5]);
		int offset = Integer.parseInt(args[6]);
		Properties props = new Properties();
		props.put("bootstrap.servers", bootstrapservers);
		props.put("group.id", applicationId);
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, Message> consumer = new KafkaConsumer<>(props);
		// consumer.subscribe(Arrays.asList(topic)); //"deviceInfoTopic"
		TopicPartition topicPartition = new TopicPartition(topic, partition);
		consumer.assign(Arrays.asList(topicPartition));

		consumer.seek(new TopicPartition(topic, partition), offset);
		consumer.close();
		return "SUCCESS";
	}

	public static void main(String[] args) { 
		//KafkaOffsetModify seek bootstrapservers applicationId topic partition offset
		//KafkaOffsetModify seek breath:9092 streams-wordcount streams-file-input 0 0
		System.out.println(args[1]);
		if ("seek".equals(args[1])) {
			System.out.println(seek(args));
		}

	}
}