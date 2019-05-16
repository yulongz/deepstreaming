package sparklearn.rdd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.fs.FileUtil;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;
import sparklearn.util.PrintUtil;
import sparklearn.util.PrintUtilPro;

public class JavaAPILearn {

	@SuppressWarnings("unused")
	public static void main(String[] args) {

		//
		SparkConf conf = new SparkConf().setAppName("JavaApiLearn").setMaster("local");
		@SuppressWarnings("resource")
		JavaSparkContext jsc = new JavaSparkContext(conf);

		// List to RDD
		List<String> list = new ArrayList<String>();
		list.add("11,22,33,44,55");
		list.add("aa,bb,cc,dd,ee");
		list.add("aa,bb,cc,dd,ee");
		JavaRDD<String> jrl = jsc.parallelize(list);

		List<String> list1 = new ArrayList<String>();
		list1.add("AA,BB,CC,DD,EE");
		list1.add("11,22,33,44,55");
		JavaRDD<String> jrl1 = jsc.parallelize(list1);

		// file to RDD
		JavaRDD<String> jrf = jsc.textFile("data/README.md");
		// PrintUtilPro.printList(jrf.collect());

		// pairrdd
		JavaPairRDD<String, Integer> jPRDD = jrf.mapToPair(new PairFunction<String, String, Integer>() {

			private static final long serialVersionUID = 1L;

			public Tuple2<String, Integer> call(String s) throws Exception {
				return new Tuple2<String, Integer>(s.split("\\s+")[0], 1);
			}
		});
		JavaPairRDD<String, Integer> jPRDDCopy = jPRDD;

		// count test
		// System.out.println(jrl.count());

		// collect test
		// System.out.println(jrl.collect());

		// save test
		// LocalFileUtil.delDir("data/README.md.save");
		// jrl.saveAsTextFile("data/README.md.save");

		// map test
		// PrintUtil.printArrayList(mapLearn(jrl).collect());
		// PrintUtilPro.printList(mapLearn(jrl).collect());

		// flatMap test
		// PrintUtil.printList(flatMapLearn(jrl).collect());
		// PrintUtilPro.printList(flatMapLearn(jrl).collect());

		// filter test
		// PrintUtil.printList(filterLearn(jrl).collect());
		// PrintUtilPro.printList(filterLearn(jrl).collect());

		// union test
		// PrintUtil.printList(unionLearn(jrl,jrl1).collect());
		// PrintUtilPro.printList(unionLearn(jrl,jrl1).collect());

		// intersection test
		// PrintUtil.printList(intersectionLearn(jrl, jrl1).collect());
		// PrintUtilPro.printList(intersectionLearn(jrl, jrl1).collect());

		// subtract
		// PrintUtil.printList(subtractLearn(jrl, jrl1).collect());
		// PrintUtilPro.printList(subtractLearn(jrl, jrl1).collect());

		// distinct
		// PrintUtil.printList(distinctLearn(jrl).collect());
		// PrintUtilPro.printList(distinctLearn(jrl).collect());

		// groupbykey test
		// PrintUtil.printPairListTI(groupByKeyLearn(jPRDD).collect());
		// PrintUtilPro.printList(groupByKeyLearn(jPRDD).collect());

		// reducebykey test
		// PrintUtil.printPairListT(reduceByKeyLearn(jPRDD).collect());
		// PrintUtilPro.printList(reduceByKeyLearn(jPRDD).collect());

		// sortByKey test
		//PrintUtil.printPairListT(sortByKeyLearn(jPRDD).collect());
	   //PrintUtilPro.printList(sortByKeyLearn(jPRDD).collect());
		
		// mapvalues test
		// PrintUtil.printPairListT(mapValuesLearn(jPRDD).collect());
		// PrintUtilPro.printList(mapValuesLearn(jPRDD).collect());

		// join test
		// PrintUtil.printPairListST(joinLearn(jPRDD,jPRDDCopy).collect());
		// PrintUtilPro.printList(joinLearn(jPRDD,jPRDDCopy).collect());

		// cogroup test
		// PrintUtil.printPairListTII(cogroupLearn(jPRDD,jPRDDCopy).collect());
		// PrintUtilPro.printList(cogroupLearn(jPRDD,jPRDDCopy).collect());
	}

	// map
	public static JavaRDD<String[]> mapLearn(JavaRDD<String> jRDD) {
		JavaRDD<String[]> jr = jRDD.map(new Function<String, String[]>() {

			private static final long serialVersionUID = 1L;

			public String[] call(String v1) throws Exception {
				return v1.split(",");
			}
		});
		return jr;
	}

	// flatmap
	public static JavaRDD<String> flatMapLearn(JavaRDD<String> jRDD) {
		JavaRDD<String> jr = jRDD.flatMap(new FlatMapFunction<String, String>() {

			private static final long serialVersionUID = 1L;

			public List<String> call(String v1) throws Exception {
				return Arrays.asList(v1.split(","));
			}
		});
		return jr;
	}

	// filter
	public static JavaRDD<String> filterLearn(JavaRDD<String> jRDD) {

		JavaRDD<String> jr = jRDD.filter(new Function<String, Boolean>() {

			private static final long serialVersionUID = 1L;

			public Boolean call(String v1) throws Exception {
				if (v1.contains("a"))
					return true;
				return false;
			}
		});
		return jr;
	}

	// union
	public static JavaRDD<String> unionLearn(JavaRDD<String> jRDD1, JavaRDD<String> jRDD2) {
		return jRDD1.union(jRDD2);
	}

	// intersection
	public static JavaRDD<String> intersectionLearn(JavaRDD<String> jRDD1, JavaRDD<String> jRDD2) {
		return jRDD1.intersection(jRDD2);
	}

	// subtract
	public static JavaRDD<String> subtractLearn(JavaRDD<String> jRDD1, JavaRDD<String> jRDD2) {
		return jRDD1.subtract(jRDD2);
	}

	// distinct
	public static JavaRDD<String> distinctLearn(JavaRDD<String> jRDD1) {
		return jRDD1.distinct();
	}

	// groupbykey
	public static JavaPairRDD<String, Iterable<Integer>> groupByKeyLearn(JavaPairRDD<String, Integer> jPRDD) {
		JavaPairRDD<String, Iterable<Integer>> groupByKeyRDD = jPRDD.groupByKey();
		return groupByKeyRDD;
	}

	// reducebykey
	public static JavaPairRDD<String, Integer> reduceByKeyLearn(JavaPairRDD<String, Integer> jPRDD) {
		JavaPairRDD<String, Integer> reduceByKeyRDD = jPRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {

			private static final long serialVersionUID = 1L;

			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1 + v2;
			}
		});
		return reduceByKeyRDD;
	}

	// sortbykey
	public static JavaPairRDD<String, Integer> sortByKeyLearn(JavaPairRDD<String, Integer> jPRDD) {
		JavaPairRDD<String, Integer> sortByKeyRDD = jPRDD.sortByKey();
		return sortByKeyRDD;
	}

	// mapValues
	public static JavaPairRDD<String, Integer> mapValuesLearn(JavaPairRDD<String, Integer> jPRDD) {
		JavaPairRDD<String, Integer> groupByKeyRDD = jPRDD.mapValues(new Function<Integer, Integer>() {

			private static final long serialVersionUID = 1L;

			public Integer call(Integer v1) throws Exception {
				return v1 * 10;
			}
		});
		return groupByKeyRDD;
	}

	// join
	public static JavaPairRDD<String, Tuple2<Integer, Integer>> joinLearn(JavaPairRDD<String, Integer> jRDD1,
			JavaPairRDD<String, Integer> jRDD2) {
		return (JavaPairRDD<String, Tuple2<Integer, Integer>>) jRDD1.join(jRDD2);
	}

	// cogroup
	public static JavaPairRDD<String, Tuple2<Iterable<Integer>, Iterable<Integer>>> cogroupLearn(
			JavaPairRDD<String, Integer> jRDD1, JavaPairRDD<String, Integer> jRDD2) {
		return (JavaPairRDD<String, Tuple2<Iterable<Integer>, Iterable<Integer>>>) jRDD1.cogroup(jRDD2);
	}

}
