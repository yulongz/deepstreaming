/** 
 * Project Name:kafkastreams 
 * File Name:MyProcessorSupplier.java 
 * Package Name:kafkastreams.processor.demo 
 * Date:2017年8月23日下午5:08:50 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package kafkastreams.processor.demo;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.ProcessorSupplier;

/**
 * ClassName:MyProcessorSupplier <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月23日 下午5:08:50 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */

//功能：
//1、每条record补上"!"
//2、每5秒钟统计一次5秒内的记录数
public class MyProcessorSupplier implements ProcessorSupplier<String, String> {

	@Override
	public Processor<String, String> get() {
		return new Processor<String, String>() {

			private ProcessorContext context;
			private Integer count;
			private Integer countTime = 5000;
			private String partition;

			@Override
			public void init(ProcessorContext context) {
				this.context = context;
				this.count = 0;
				this.countTime = 5000;
				this.context.schedule(this.countTime);
			}

			@Override
			public void process(String key, String value) {
				String newValue = value + "!";
				context.forward(key, newValue);
				count++;
				//context.commit();
				this.partition = ""+this.context.partition();
			}

			@Override
			public void punctuate(long timestamp) {
				System.out.println("partitionId:"+partition+",每"+this.countTime+"ms的数据记录数为："+this.count);
				this.count=0;
			}

			@Override
			public void close() {
			}

		};
	}

}
