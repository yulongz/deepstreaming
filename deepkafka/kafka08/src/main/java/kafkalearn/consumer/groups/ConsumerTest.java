/** 
 * Project Name:kafkalearn 
 * File Name:ConsumerTest.java 
 * Package Name:com.test.groups 
 * Date:2017年8月16日上午10:38:17 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package kafkalearn.consumer.groups;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;

/**
 * ClassName:ConsumerTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月16日 上午10:38:17 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings("rawtypes")
public class ConsumerTest implements Runnable {

	private KafkaStream m_stream;
	private int m_threadNumber;

	public ConsumerTest(KafkaStream a_stream, int a_threadNumber) {
		m_threadNumber = a_threadNumber;
		m_stream = a_stream;
	}

	@SuppressWarnings("unchecked")
	public void run() {
		//System.out.println(m_stream.last().message());
		ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
		if(it instanceof ConsumerIterator<?,?>){
			System.out.println("it instanceof ConsumerIterator<?,?>");
		}
		if(it == null){
			System.out.println("it == null");
		}
		MessageAndMetadata<byte[], byte[]> next;
		System.err.println("========================================1");
		//System.err.println("stream.iterator:"+it.size());
		System.err.println("========================================2");
		int messageNum = 0;
		while (it.hasNext()) {
			messageNum++;
			next = it.next();
			System.err.println("===========messageNum:" + messageNum);
			System.out.println("Thread " + m_threadNumber + ": " + new String(next.message()));
		}

		System.out.println("Shutting down Thread: " + m_threadNumber);
	}
}
