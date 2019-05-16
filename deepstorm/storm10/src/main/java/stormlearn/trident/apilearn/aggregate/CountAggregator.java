/** 
 * Project Name:stormlearn 
 * File Name:CountAggregator.java 
 * Package Name:stormlearn.trident.apilearn.aggregate 
 * Date:2017年8月8日下午4:12:53 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.aggregate;

import org.apache.storm.trident.operation.BaseAggregator;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Values;


/**
 * ClassName:CountAggregator <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月8日 下午4:12:53 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings({ "serial" })
public class CountAggregator extends BaseAggregator<CountAggregator.State> {

    static class State {
        long count = 0;
    }
    
    @Override
    public State init(Object batchId, TridentCollector collector) {
        return new State();
    }

    @Override
    public void aggregate(State state, TridentTuple tuple, TridentCollector collector) {
        state.count++;
    }

    @Override
    public void complete(State state, TridentCollector collector) {
        collector.emit(new Values(state.count));
    }
    
}
