/** 
 * Project Name:sparklearn 
 * File Name:NetworkWordCount.java 
 * Package Name:sparklearn.streaming 
 * Date:2017年7月17日上午9:19:13 
 * sky.zyl@hotmail.com
*/

package sparklearn.streaming;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import scala.Tuple2;

import org.apache.spark.streaming.Durations;

/**
 * ClassName:NetworkWordCount <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月17日 上午9:19:13 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class NetworkWordCount {

	public static void main(String[] args) {
		networkWC();
	}

	public static void networkWC() {
		// Create a local StreamingContext with two working thread and batch
		// interval of 5 second
		SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount");
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));
      //jssc.sparkContext();
		
		// Create a DStream that will connect to hostname:port, like
		// localhost:9999
		JavaReceiverInputDStream<String> lines = jssc.socketTextStream("localhost", 9999);

		// Split each line into words
		JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
			@Override
			public Iterable<String> call(String x) {
				return Arrays.asList(x.split(" "));
			}
		});

		// Count each word in each batch
		JavaPairDStream<String, Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {
			@Override
			public Tuple2<String, Integer> call(String s) {
				return new Tuple2<String, Integer>(s, 1);
			}
		});
		JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
			@Override
			public Integer call(Integer i1, Integer i2) {
				return i1 + i2;
			}
		});

		// Print the first ten elements of each RDD generated in this DStream to
		// the console
		wordCounts.print();

		jssc.start(); // Start the computation
		jssc.awaitTermination(); // Wait for the computation to terminate

	}

}
