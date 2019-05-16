/** 
 * Project Name:stormlearn 
 * File Name:MySplit.java 
 * Package Name:stormlearn.trident.apilearn.flatmap 
 * Date:2017年8月5日下午11:19:20 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.flatmap;

import java.util.ArrayList;
import java.util.List;

import org.apache.storm.trident.operation.FlatMapFunction;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Values;

/**
 * ClassName:MySplit <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月5日 下午11:19:20 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings("serial")
public class MySplit implements FlatMapFunction {
	public Iterable<Values> execute(TridentTuple input) {
		List<Values> valuesList = new ArrayList<Values>();
		for (String word : input.getString(0).split(" ")) {
			valuesList.add(new Values(word, input.getString(0)));
		}
		return valuesList;
	}
}
