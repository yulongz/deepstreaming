/** 
 * Project Name:stormlearn 
 * File Name:CountCombinerAggregator.java 
 * Package Name:stormlearn.trident.apilearn.aggregate 
 * Date:2017年8月7日下午8:24:41 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.aggregate;

import org.apache.storm.trident.operation.CombinerAggregator;
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
public class CountCombinerAggregator implements CombinerAggregator<Integer> {

	@Override
	public Integer init(TridentTuple tuple) {
		return 1;
	}

	public Integer combine(Integer val1, Integer val2) {
		return val1 + val2;
	}

	@Override
	public Integer zero() {
		return 0;
	}

}
