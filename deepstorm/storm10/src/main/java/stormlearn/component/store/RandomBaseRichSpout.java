/** 
 * Project Name:stormlearn 
 * File Name:WordSpout.java 
 * Package Name:stormlearn.component 
 * Date:2017年7月25日下午5:20:08 
 * sky.zyl@hotmail.com
*/

package stormlearn.component.store;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import stormlearn.util.print.PrintHelper;

/**
 * ClassName:WordSpout <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月25日 下午5:20:08 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings("serial")
public class RandomBaseRichSpout extends BaseRichSpout {
	private SpoutOutputCollector collector;
	private static final String[] msgs = new String[] { "apache", "storm", "needs", "javadoc" };
	private static final Random random = new Random();
	private AtomicInteger counter;
	private int msgId;
	private String _componentId;
	private int _taskId;
	private boolean isDebug;

	public RandomBaseRichSpout() {
		this.isDebug = false;
	}

	public RandomBaseRichSpout(boolean isDebug) {
		this.isDebug = isDebug;
	}

	@SuppressWarnings("rawtypes")
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
		this.counter = new AtomicInteger();
		this._taskId = context.getThisTaskId();
		this._componentId = context.getThisComponentId();
	}

	public void nextTuple() {
		Utils.sleep(1000);
		String sentence = msgs[random.nextInt(4)];
		// 如果使用local
		// variable，不同的executor记录的不同的msgId值。这里如果使用共同的counter，则需要改造成共享的，如zookeeper或者redis
		msgId = this.counter.getAndIncrement();
		collector.emit(new Values(sentence,"1"), msgId);
		if (isDebug) {
			PrintHelper.print(
					"componentId:" + _componentId + ",taskId:" + this._taskId + ",[" + msgId + "] " + sentence + ".");
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("key","value"));
	}

	public void ack(Object msgId) {
		// System.err.println("ack:" + msgId);
	};

	public void fail(Object msgId) {
		// System.err.println("fail:" + msgId);
	};

}
