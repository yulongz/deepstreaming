/** 
 * Project Name:sparklearn 
 * File Name:PersisitTest.java 
 * Package Name:sparklearn.rdd 
 * Date:2017年7月14日上午11:03:52 
 * sky.zyl@hotmail.com
*/

package sparklearn.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.storage.StorageLevel;

import sparklearn.util.PrintUtilPro;

/**
 * ClassName:PersisitTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月14日 上午11:03:52 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class PersisitTest {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("SparkWordCount").setMaster("local");

		JavaSparkContext jsc = new JavaSparkContext(conf);

		JavaRDD<String> list = jsc.textFile("data/README.md");
		
		PrintUtilPro.printList(list.collect());//1
		list.persist(StorageLevel.MEMORY_ONLY_2());//2
		//list.cache();//触发persist也是懒执行
		PrintUtilPro.printList(list.collect());//3
		PrintUtilPro.printList(list.collect());//4
		
		//1这一步并没有查找cache
		//2这一步的cache是懒执行，在下一步action操作的时候才会触发执行
		//3虽然触发了cache，但是这一步会先查找有没有cache，然后进行上一部的装载cache，然后执行collect。这一步的计算在cache之前，所以并没有用到cache。详情见类CacheManager
		//4这一步会先从cachemanager查找有没有cache，有直接使用cache，没有则计算
		
		//日志如下：rdd_1_0为cache
		/*...
		...
		##############################this is collect data
		...
		...
		2017-07-14 11:07:59,054 [Executor task launch worker-0] DEBUG spark.CacheManager logDebug - Looking for partition rdd_1_0
		2017-07-14 11:07:59,055 [Executor task launch worker-0] DEBUG storage.BlockManager logDebug - Getting local block rdd_1_0
		2017-07-14 11:07:59,056 [Executor task launch worker-0] DEBUG storage.BlockManager logDebug - Block rdd_1_0 not registered locally
		...
		...
		2017-07-14 11:07:59,061 [Executor task launch worker-0] DEBUG storage.BlockManager logDebug - Block rdd_1_0 not found
		2017-07-14 11:07:59,062 [Executor task launch worker-0] INFO  spark.CacheManager logInfo - Partition rdd_1_0 not found, computing it
		2017-07-14 11:07:59,063 [Executor task launch worker-0] INFO  rdd.HadoopRDD logInfo - Input split: file:/home/hadoop/gitbonc/sparklearn/data/README.md:0+42
		...
		...
		2017-07-14 11:07:59,071 [Executor task launch worker-0] INFO  storage.MemoryStore logInfo - Block rdd_1_0 stored as values in memory (estimated size 240.0 B, free 1140.3 MB)
		2017-07-14 11:07:59,072 [dispatcher-event-loop-1] INFO  storage.BlockManagerInfo logInfo - Added rdd_1_0 in memory on localhost:33541 (size: 240.0 B, free: 1140.4 MB)
		2017-07-14 11:07:59,073 [Executor task launch worker-0] DEBUG storage.BlockManagerMaster logDebug - Updated info of block rdd_1_0
		2017-07-14 11:07:59,073 [Executor task launch worker-0] DEBUG storage.BlockManager logDebug - Told master about block rdd_1_0
		2017-07-14 11:07:59,073 [Executor task launch worker-0] DEBUG storage.BlockManager logDebug - Put block rdd_1_0 locally took  3 ms
		2017-07-14 11:07:59,076 [Executor task launch worker-0] DEBUG storage.BlockManager logDebug - Fetched peers from master: []
		2017-07-14 11:07:59,077 [Executor task launch worker-0] DEBUG storage.BlockManager logDebug - Replicating rdd_1_0 of 53 bytes to 0 peer(s) took 3 ms
		2017-07-14 11:07:59,078 [Executor task launch worker-0] WARN  storage.BlockManager logWarning - Block rdd_1_0 replicated to only 0 peer(s) instead of 1 peers
		2017-07-14 11:07:59,079 [Executor task launch worker-0] DEBUG storage.BlockManager logDebug - Put block rdd_1_0 remotely took  5 ms
		2017-07-14 11:07:59,079 [Executor task launch worker-0] DEBUG storage.BlockManager logDebug - Putting block rdd_1_0 with replication took  9 ms
		2017-07-14 11:07:59,086 [Executor task launch worker-0] INFO  executor.Executor logInfo - Finished task 0.0 in stage 1.0 (TID 1). 2673 bytes result sent to driver
		##############################this is collect data
		...
		...
		2017-07-14 11:07:59,148 [Executor task launch worker-0] DEBUG spark.CacheManager logDebug - Looking for partition rdd_1_0
		2017-07-14 11:07:59,148 [Executor task launch worker-0] DEBUG storage.BlockManager logDebug - Getting local block rdd_1_0
		2017-07-14 11:07:59,148 [Executor task launch worker-0] DEBUG storage.BlockManager logDebug - Level for block rdd_1_0 is StorageLevel(false, true, false, true, 2)
		2017-07-14 11:07:59,148 [Executor task launch worker-0] DEBUG storage.BlockManager logDebug - Getting block rdd_1_0 from memory
		2017-07-14 11:07:59,149 [Executor task launch worker-0] INFO  storage.BlockManager logInfo - Found block rdd_1_0 locally
		...
		...
		##############################this is collect data*/

	}

}
