/** 
 * Project Name:stormlearn 
 * File Name:SlidingWindowBolt.java 
 * Package Name:stormlearn.windows.wordcount 
 * Date:2017年8月2日下午8:38:12 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.component.windows.wordcount;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.windowing.TupleWindow;

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
public class CountTumblingWindowedBolt extends BaseWindowedBolt {

	private OutputCollector collector;
	private String word;
	private int count;
	private HashMap<String, Integer> counter = new HashMap<String, Integer>();

	@SuppressWarnings("rawtypes")
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
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
			
			// newTuples count++
			for (Tuple tuple : newTuples) {
				word = tuple.getString(0);
				if (counter.get(word) == null)
					count = 0;
				count++;
				counter.put(word, count);
			}

			// expiredTuples count--
//			for (Tuple tuple : expiredTuples) {
//				word = tuple.getString(0);
//				if (counter.get(word) == null) {
//					//do noting
//				} else if (counter.get(word) == 1) {
//					counter.remove(word);
//				} else {
//					count--;
//					counter.put(word, count);
//				}
//			}
			
			// collector.emit
			for (Entry<String, Integer> s : counter.entrySet()) {
				collector.emit(new Values(s.getKey(), s.getValue()));
			}
			counter.clear();
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word", "count"));
	}

}
