/** 
 * Project Name:stormlearn 
 * File Name:CountCombinerAggregator.java 
 * Package Name:stormlearn.trident.apilearn.aggregate 
 * Date:2017年8月7日下午8:24:41 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.aggregate;

import org.apache.storm.trident.operation.ReducerAggregator;
import org.apache.storm.trident.tuple.TridentTuple;

/**
 * ClassName:CountCombinerAggregator <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月7日 下午8:24:41 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings({ "serial" })
public class CountReduceAggregator implements ReducerAggregator<Integer> {

	@Override
	public Integer init() {
		return 0;
	}

	@Override
	public Integer reduce(Integer curr, TridentTuple tuple) {
		return curr + 1;
	}

}
