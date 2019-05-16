/** 
 * Project Name:kafkalearn 
 * File Name:ProducerDemo.java 
 * Package Name:kafkalearn.producer 
 * Date:2017年8月14日上午11:55:13 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package kafkastreams.producer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * ClassName:ProducerDemo <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月14日 上午11:55:13 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings({ "rawtypes", "resource", "unchecked" })
public class ProducerDemo {
	public static void main(String[] args) {
		// 消息发送方式：异步发送还是同步发送
		boolean isAsync = args.length == 0 || !args[0].trim().equalsIgnoreCase("sync");

		Properties props = new Properties();
		// Kafka服务端的主机名和端口号
		props.put("bootstrap.servers", "breath:9092");
		// 客户端的ID
		props.put("client.id", "DemoProducer");
		// 指定key和value的序列化器
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		// 初始化producer核心类
		KafkaProducer producer = new KafkaProducer<>(props);
		// 定义topic
		//String topic = "streams-dslapi-input";
		String topic = "test";
		int messageNo = 1;// messageNo为发送消息的key
		boolean isWhile = true;
		while (isWhile) {
			String massageKey = ""+messageNo;
			String messageValue = "Message_" + messageNo;// messagNo为发送消息的value
			long startTime = System.currentTimeMillis();
			if (isAsync) {// 异步发送消息
				// 第一个参数是ProducerRecord对象，封装了目标topic、消息的的key、消息的value
				// 第二个参数是一个CallBack对象，当生产者接收到kafka发来的ACK确认消息的时候，会调用此CallBack对象的onCompletion()方法，实现回调功能。
				producer.send(new ProducerRecord<>(topic, massageKey, messageValue),
						new DemoCallBack(startTime, massageKey, messageValue));
				System.out.println("Send message: (" + massageKey + ", " + messageValue + ")");
			} else {// 同步发送消息
				try {
					// KafkaProducer.send()方法的返回值类型是Future<RecordMetadata>
					// 这里通过Future.get()方法，阻塞
					producer.send(new ProducerRecord<>(topic, massageKey, messageValue)).get();
					System.out.println("Send message: (" + massageKey + ", " + messageValue + ")");
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
			++messageNo;// 递增消息的key
			if(messageNo>1) isWhile = false;
		}
	}
}

class DemoCallBack implements Callback {
	private final long startTime;
	private final String key;
	private final String message;

	public DemoCallBack(long startTime, String key, String message) {
		this.startTime = startTime;
		this.key = key;
		this.message = message;
	}

	public void onCompletion(RecordMetadata metadata, Exception exception) {
		long elapsedTime = System.currentTimeMillis() - startTime;
		if (metadata != null) {
			System.out.println("message(" + key + "," + message + ") sent to partition(" + metadata.partition() + "), "
					+ "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
		} else {
			exception.printStackTrace();
		}
	}

}