
package kafkastreams.dsl.api.transform;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.kstream.TransformerSupplier;
import org.apache.kafka.streams.processor.ProcessorContext;

//功能：
//1、每条record补上"!"
//2、每5秒钟统计一次5秒内的记录数
public class MyTransformerSupplier implements TransformerSupplier<String, String ,KeyValue<String,Integer>> {

	@Override
	public Transformer<String, String, KeyValue<String, Integer>> get() {
		return new Transformer<String, String, KeyValue<String,Integer>>() {

			private ProcessorContext context;
			private Integer count;
			private Integer countTime;
			private String partition;

			@Override
			public void init(ProcessorContext context) {
				this.context = context;
				this.count = 0;
				this.countTime = 5000;
				this.context.schedule(this.countTime);
			}

			@Override
			public KeyValue<String, Integer> transform(String key, String value) {
				count++;
				this.partition = ""+this.context.partition();
				return KeyValue.pair(partition, this.count);
			}

			@Override
			public KeyValue<String,Integer> punctuate(long timestamp) {
				System.out.println("partitionId:"+partition+",每"+this.countTime+"ms的数据记录数为："+this.count);
				return KeyValue.pair(Long.toString(timestamp), this.count);
			}

			@Override
			public void close() {
			}

		};
	}

}
