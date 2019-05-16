package sparklearn.rdd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.storage.StorageLevel;

import scala.Tuple2;
import sparklearn.util.LocalFileUtil;
import sparklearn.util.PrintUtil;

public class WordCount {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		SparkConf conf = new SparkConf().setAppName("SparkWordCount").setMaster("local");

		JavaSparkContext jsc = new JavaSparkContext(conf);
		
		JavaRDD<String> list = jsc.textFile("data/README.md");
		
		JavaRDD<String> flatMapRDD = list.flatMap(new FlatMapFunction<String, String>() {

				private static final long serialVersionUID = 1L;

				public List<String> call(String v1) throws Exception {
					return Arrays.asList(v1.split("\\s+"));
				}
			});
		
		JavaPairRDD<String,Integer> pairRDD = flatMapRDD.mapToPair(new PairFunction<String, String, Integer>() {

			private static final long serialVersionUID = 1L;

			public Tuple2<String, Integer> call(String s) throws Exception {
				return new Tuple2<String, Integer>(s, 1);
			}
		});
		
		JavaPairRDD<String,Integer> reduceByKeyRDD = pairRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {

				private static final long serialVersionUID = 1L;

				public Integer call(Integer v1, Integer v2) throws Exception {
					return v1 + v2;
				}
			});
		
		JavaRDD<String> textRDD = reduceByKeyRDD.map(new Function<Tuple2<String,Integer>,String>(){

			private static final long serialVersionUID = 1L;

			public String call(Tuple2<String,Integer> tuple2) throws Exception{
				return tuple2._1()+"|"+Integer.toString(tuple2._2());
			}
		});		
		PrintUtil.printPairListT(reduceByKeyRDD.collect());
		System.out.println("================");		
		reduceByKeyRDD.persist(StorageLevel.MEMORY_ONLY_2());
		JavaPairRDD<String,Integer> reduceByKeyRDDN = reduceByKeyRDD;
		PrintUtil.printPairListT(reduceByKeyRDDN.collect());

//		LocalFileUtil.delDir("data/wordcount");
//		textRDD.saveAsTextFile("data/wordcount");
	
	}
}
