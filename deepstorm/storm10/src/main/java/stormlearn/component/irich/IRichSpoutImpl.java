/** 
 * Project Name:stormlearn 
 * File Name:IRichSpoutImpl.java 
 * Package Name:stormlearn.component.irich 
 * Date:2017年8月2日上午9:41:40 
 * sky.zyl@hotmail.com
*/

package stormlearn.component.irich;

import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;

/**
 * ClassName:IRichSpoutImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年8月2日 上午9:41:40 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
@SuppressWarnings("serial")
public class IRichSpoutImpl implements IRichSpout {

	// open()方法在该组件的一个任务在集群的工作进程内被初始化时调用，提供了Spout执行所需要的环境。
	// conf参数是这个Spout的Storm配置，提供给拓扑与这台主机的集群配置一起进行合并。
	// Context参数可以用来获取关于这个任务在拓扑中位置信息，包括该任务的id、该任务的组件id、输入和输出信息等。
	// collector参数是收集器，用于从这个Spout发射元组。元组可以随时被发射，包括open()和close()方法。
	// 收集器是线程安全的，应该作为这个Spout对象的实例变量进行保存。
	@SuppressWarnings("rawtypes")
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
	}

	// close()方法当一个ISpout即将关闭时被调用。
	// 不能保证close()方法一定会被调用，因为Supervisor可以对集群的工作进程使用kill -9命令强制杀死进程命令。
	// 如果在本地模式下运行Storm，当拓扑被杀死的时候，可以保证close()方法一定会被调用。
	public void close() {
	}

	// activate()方法当Spout已经从失效模式中激活时被调用。该Spout的nextTuple()方法很快会调用。
	// 当使用Storm客户端操作拓扑时，Spout可以在失效状态之后变成激活模式。
	public void activate() {
	}

	// 当Spout已经失效时被调用。当Spout失效期间，nextTuple不会被调用。Spout将来可能会也可能不会被重新激活。
	public void deactivate() {
	}

	// 当调用nextTuple()方法时，Storm要求Spout发射元组到输出收集器OutputCollecotr。
	// nextTuple()方法应该是非阻塞的，所以，如果Spout没有元组可以发射，该方法应该返回。
	// nextTuple()、ack()和fail()方法都在Spout任务的单一线程内紧密循环被调用。
	// 当没有元组可以发射时，可以让nextTuple()去sleep很短的时间，例如1毫秒，这样就不会浪费太多的CPU资源。
	public void nextTuple() {
	}

	// Storm已经断定该Spout发射的标识符为msgId的元组已经被完全处理时，会调用ack方法。通常情况下，ack()方法会将该信息移出队列以防止它被重发。
	public void ack(Object msgId) {
	}

	// 该Spout发射的标识符为msgId的元组未能被完全处理时，会调用fail()方法。通常情况下，fail方法会将消息放回队列中，并在稍后重发消息。
	public void fail(Object msgId) {
	}

	// 为拓扑的所有流组件生命输出模式。
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

	// 声明指定组件大的配置。只有"topology.*"配置的一个子集可以被覆盖。当使用TopologyBuilder构建拓扑是，组件配置可以被进一步覆盖。
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
