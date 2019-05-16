/** 
 * Project Name:stormlearn 
 * File Name:SlidingWindowBolt.java 
 * Package Name:stormlearn.windows.wordcount 
 * Date:2017年8月2日下午8:38:12 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.component.windows.timestamp;

import java.util.List;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.windowing.TupleWindow;

import stormlearn.util.print.PrintTimestamp;

/**
 * ClassName:SlidingWindowBolt <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月2日 下午8:38:12 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings("serial")
public class PrintSlidingWindowedBolt extends BaseWindowedBolt {

	private long ts;
	private String sentence;

	@SuppressWarnings("rawtypes")
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
	}

	public void execute(TupleWindow inputWindow) {
		{
			/*
			 * The inputWindow gives a view of (a) all the events in the window
			 * (b) events that expired since last activation of the window (c)
			 * events that newly arrived since last activation of the window
			 */
			List<Tuple> tuplesInWindow = inputWindow.get();
			List<Tuple> newTuples = inputWindow.getNew();
			List<Tuple> expiredTuples = inputWindow.getExpired();

			// LOG.debug("Events in current window: " + tuplesInWindow.size());
			/*
			 * Instead of iterating over all the tuples in the window to compute
			 * the sum, the values for the new events are added and old events
			 * are subtracted. Similar optimizations might be possible in other
			 * windowing computations.
			 */

			System.out.println("Events in current window: " + tuplesInWindow.size());
			System.out.println("Events in new window: " + newTuples.size());
			System.out.println("Events in expired window: " + expiredTuples.size());
			System.out.println("windowcount##################");
			for (Tuple tuple : tuplesInWindow) {
				ts = tuple.getLongByField("ts");
				sentence = tuple.getStringByField("sentence");
				System.out.println(PrintTimestamp.getTSString() + "|" + PrintTimestamp.getTSString(ts) + "|" + ts + ":"
						+ sentence);
			}
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word", "count"));
	}

}
