package sparklearn.util;

import java.util.Iterator;
import java.util.List;
import scala.Tuple2;

public class PrintUtil {

	public static void printArrayList(List<String[]> list) {

		for (String[] line : list) {
			for (String word : line) {
				System.out.print(word);
			}
			System.out.println("");
		}
	}

	public static void printList(List<String> list) {
		for (String word : list) {
			System.out.println(word);
		}
	}

	public static void printPairListTI(List<Tuple2<String, Iterable<Integer>>> list) {
		for (Tuple2<String, Iterable<Integer>> tuple2 : list) {
			System.out.print(tuple2._1() + ",");
			Iterator<?> iter = tuple2._2().iterator();
			System.out.print("[");
			System.out.print((Integer) iter.next());
			while (iter.hasNext()) {
				System.out.print(",");
				System.out.print((Integer) iter.next());
			}
			System.out.println("]");
		}
	}

	public static void printPairListT(List<Tuple2<String, Integer>> list) {
		for (Tuple2<String, Integer> tuple2 : list) {
			System.out.println(tuple2._1() + "," + Integer.toString(tuple2._2()));
		}
	}

	public static void printPairListST(List<Tuple2<String, Tuple2<Integer, Integer>>> list) {
		for (Tuple2<String, Tuple2<Integer, Integer>> tuple2 : list) {
			System.out.println(tuple2._1() + ",(" + Integer.toString(tuple2._2()._1()) + ","
					+ Integer.toString(tuple2._2()._2()) + ")");
		}
	}

	public static void printPairListTII(List<Tuple2<String, Tuple2<Iterable<Integer>, Iterable<Integer>>>> list) {
		for (Tuple2<String, Tuple2<Iterable<Integer>, Iterable<Integer>>> tuple21 : list) {
			System.out.print(tuple21._1() + ",([");
			for (Iterator<Integer> iter = tuple21._2()._1().iterator(); iter.hasNext();) {
				Integer integer = iter.next();
				System.out.print(integer);
			}
			System.out.print("],[");
			for (Iterator<Integer> iter = tuple21._2()._2().iterator(); iter.hasNext();) {
				Integer integer = iter.next();
				System.out.print(integer);
			}
			System.out.println("])");
		}
	}

}
