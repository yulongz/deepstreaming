package sparklearn.rdd;

/* SimpleApp.java */
import org.apache.spark.api.java.*;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;

//spark-submit --class "com.yulongz.sparklearn.java.SimpleApp" --master local[1] target/sparklearn-0.0.1-SNAPSHOT.jar

public class SimpleApp {
	public static void main(String[] args) {

		String logFile = "data/README.md"; // Should be
											// some file
											// on your
											// system
		SparkConf conf = new SparkConf().setAppName("Simple Application").setMaster("local");
		@SuppressWarnings("resource")
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> logData = sc.textFile(logFile).cache();

		long numAs = logData.filter(new Function<String, Boolean>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Boolean call(String s) {
				return s.contains("a");
			}
		}).count();

		long numBs = logData.filter(new Function<String, Boolean>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Boolean call(String s) {
				return s.contains("b");
			}
		}).count();

		System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);
	}
}