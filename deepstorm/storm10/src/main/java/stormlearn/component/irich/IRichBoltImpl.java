/** 
 * Project Name:stormlearn 
 * File Name:IRichBoltImpl.java 
 * Package Name:stormlearn.component.irich 
 * Date:2017年8月2日上午9:46:18 
 * sky.zyl@hotmail.com
*/  
  
package stormlearn.component.irich;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

/** 
 * ClassName:IRichBoltImpl <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年8月2日 上午9:46:18 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
@SuppressWarnings("serial")
public class IRichBoltImpl implements IRichBolt {

	//prepare()方法在该组件的一个任务是集群的工作进程内被初始化时调用，提供了Bolt执行时所需要的环境。
	//topoConf参数是这个Bolt的Storm配置，提供给拓扑和这台主机上的集群配置一起进行合并。
	//context参数可以用来获取关于这个任务在拓扑中的位置信息，包括该任务的id、该任务的组件id、输入输出信息等。
	//collector参数是收集器，用于从这个Bolt发射元组。元组可以随社被发射，包括prepare()和cleanup()方法。
	//收集器是线程安全的，应该作为这个Bolt对象的实例变量进行保存。
	@SuppressWarnings("rawtypes")
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
	}

	//execute()方法处理一个输入元组。元组对象包括元组来自哪个component/stream/task的元数据。
	//元组的值可以使用Tuple#getValue()进行访问。
	//IBolt没有立即处理元组，而是完全的捕获一个元组并在以后进行处理，例如，做聚合或者连接计算。
	//元组应该使用prepare方法提供的OutputCollector进行发射。使用OutputCollector在某种程度上要求所有输入元组是ack或者fail。
	//否则，Storm将无法确定来自Spout的元组什么时候会处理完成。
	//常规做法是，在executor方法结束是对输入元组调用ack方法，而IBasicBolt会自动处理该部分。
	//input参数为被处理的输入元组。
	public void execute(Tuple input) {
	}

	//cleanup方法当一个IBolt即将关闭时被调用。
	//不能保证cleanup方法一定被调用，因为Supervisor可以对集群的工作进程使用kill -9命令强制杀死进程命令。
	//如果在本地模式下运行Storm，当拓扑被杀死的时候，可以保证cleanup()方法一定会被调用。
	public void cleanup() {
	}

	// 为拓扑的所有流组件生命输出模式。
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

	// 声明指定组件大的配置。只有"topology.*"配置的一个子集可以被覆盖。当使用TopologyBuilder构建拓扑是，组件配置可以被进一步覆盖。
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
 