/** 
 * Project Name:storm2test 
 * File Name:MyAssembly.java 
 * Package Name:stormlearn.trident.apilearn.applyAssembly 
 * Date:2017年8月8日下午3:08:59 
 * Author:hadoop 
 * Email:sky.zyl@hotmail.com
*/

package stormlearn.trident.apilearn.applyassembly;

import org.apache.storm.trident.Stream;
import org.apache.storm.trident.operation.Assembly;
import org.apache.storm.trident.operation.MapFunction;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Values;

/**
 * ClassName:MyAssembly <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月8日 下午3:08:59 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class MyAssembly implements Assembly {

	public Stream apply(Stream input) {
		return input.map(toUpper);
	}

	@SuppressWarnings("serial")
	private static MapFunction toUpper = new MapFunction() {
		public Values execute(TridentTuple input) {
			return new Values(input.getStringByField("sentence").toUpperCase());
		}
	};

}
