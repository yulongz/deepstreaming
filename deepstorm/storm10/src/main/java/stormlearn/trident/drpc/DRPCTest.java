/** 
 * Project Name:stormlearn 
 * File Name:DRPCTest.java 
 * Package Name:stormlearn.trident 
 * Date:2017年7月26日下午6:53:21 
 * sky.zyl@hotmail.com
*/

package stormlearn.trident.drpc;

import org.apache.storm.Config;
import org.apache.storm.utils.DRPCClient;

/**
 * ClassName:DRPCTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月26日 下午6:53:21 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class DRPCTest {

	public static void main(String[] args) throws Exception {
		Config config = new Config();

		DRPCClient client = new DRPCClient(config, "breath", 3772);
		for (int i = 0; i < 100; i++) {
			try {
				System.out.println("DRPC Result: " + client.execute("words", "cat the dog jumped"));
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}

	}

}
