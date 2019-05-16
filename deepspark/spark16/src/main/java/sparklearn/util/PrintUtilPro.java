/** 
 * Project Name:sparklearn 
 * File Name:PrintUtilPro.java 
 * Package Name:sparklearn.util 
 * Date:2017年7月3日上午10:54:52 
 * sky.zyl@hotmail.com
*/

package sparklearn.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import scala.Tuple2;

/**
 * ClassName:PrintUtilPro <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月3日 上午10:54:52 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class PrintUtilPro {

	@SuppressWarnings("rawtypes")
	public static void printList(List<? extends Object> list) {

		if (list.size() < 1) {
			return;
		} else if (list.get(0) instanceof String[]) {
			for (Object obj : list) {
				for (String str : (String[]) obj) {
					System.out.print(str);
				}
				System.out.println("");
			}
		} else if (list.get(0) instanceof Integer[]) {

			for (Object obj : list) {
				for (Integer str : (Integer[]) obj) {
					System.out.print(str);
				}
				System.out.println("");
			}
		} else if (list.get(0) instanceof String) {
			for (Object obj : list) {
				System.out.println((String) obj);
			}
		} else if (list.get(0) instanceof Integer) {
			for (Object obj : list) {
				System.out.println(Integer.toString((Integer) obj));
			}
		} else if (list.get(0) instanceof Tuple2<?, ?>) {
			for (Object obj : list) {
				// tuple2._1()
				if (((Tuple2) obj)._1() instanceof String) {
					System.out.print((String) ((Tuple2) obj)._1());
				} else if (((Tuple2) obj)._1() instanceof Integer) {
					System.out.print((Integer) ((Tuple2) obj)._1());
				}else {
					// do something
					System.out.println("((Tuple2) obj)._1() not define");
				}
				// tuple2 k,v
				System.out.print(",");
				// tuple2._2()
				if (((Tuple2) obj)._2() instanceof String) {
					System.out.println((String) ((Tuple2) obj)._2());
				} else if (((Tuple2) obj)._2() instanceof Integer) {
					System.out.println(Integer.toString((Integer) ((Tuple2) obj)._2()));
				}else if(((Tuple2) obj)._2() instanceof Iterable<?>){
					System.out.print("[");
					String type = "";
					if (((Iterable) ((Tuple2) obj)._2()).iterator().hasNext()) {
						Object itr = ((Iterable) ((Tuple2) obj)._2()).iterator().next();
						if (itr instanceof Integer) {
							type = "Integer";
						} else if (itr instanceof String) {
							type = "String";
						}
					} else {
						type = "null";
					}
					Iterator<?> ite = ((Iterable) ((Tuple2) obj)._2()).iterator();
					switch (type) {
					case "Integer": {
						System.out.print((Integer) ite.next());
						while (ite.hasNext()) {
							System.out.print(",");
							System.out.print((Integer) ite.next());
						}
						break;
					}
					case "String": {
						System.out.print((String) ite.next());
						while (ite.hasNext()) {
							System.out.print(",");
							System.out.print((String) ite.next());
						}
						break;
					}
					case "null": {
						// do nothing
					}
					}
					System.out.println("]");
				} else if (((Tuple2) obj)._2() instanceof Tuple2<?, ?>) {
					System.out.print("(");
					// tuple2._2()._1()
					if (((Tuple2) ((Tuple2) obj)._2())._1() instanceof String) {
						System.out.print((String) ((Tuple2) ((Tuple2) obj)._2())._1());
					} else if (((Tuple2) ((Tuple2) obj)._2())._1() instanceof Integer) {
						System.out.print((Integer) ((Tuple2) ((Tuple2) obj)._2())._1());
					} else if(((Tuple2) ((Tuple2) obj)._2())._1() instanceof Iterable<?>){
						System.out.print("[");
						String type = "";
						if (((Iterable) ((Tuple2) ((Tuple2) obj)._2())._1()).iterator().hasNext()) {
							Object itr = ((Iterable) ((Tuple2) ((Tuple2) obj)._2())._1()).iterator().next();
							if (itr instanceof Integer) {
								type = "Integer";
							} else if (itr instanceof String) {
								type = "String";
							}
						} else {
							type = "null";
						}
						Iterator<?> ite = ((Iterable) ((Tuple2) ((Tuple2) obj)._2())._1()).iterator();
						switch (type) {
						case "Integer": {
							System.out.print((Integer) ite.next());
							while (ite.hasNext()) {
								System.out.print(",");
								System.out.print((Integer) ite.next());
							}
							break;
						}
						case "String": {
							System.out.print((String) ite.next());
							while (ite.hasNext()) {
								System.out.print(",");
								System.out.print((String) ite.next());
							}
							break;
						}
						case "null": {
							// do nothing
						}
						}
						System.out.print("]");
					}else {
						System.out.print("((Tuple2) ((Tuple2) obj)._2())._1() type not define");
					}
					// tuple2._2() k,v
					System.out.print(",");
					// tuple2._2()._2()
					if (((Tuple2) ((Tuple2) obj)._2())._2() instanceof String) {
						System.out.print((String) ((Tuple2) ((Tuple2) obj)._2())._2());
					} else if (((Tuple2) ((Tuple2) obj)._2())._2() instanceof Integer) {
						System.out.print((Integer) ((Tuple2) ((Tuple2) obj)._2())._2());
					} else if (((Tuple2) ((Tuple2) obj)._2())._2() instanceof Iterable<?>) {
						System.out.print("[");
						String type = "";
						if (((Iterable) ((Tuple2) ((Tuple2) obj)._2())._2()).iterator().hasNext()) {
							Object itr = ((Iterable) ((Tuple2) ((Tuple2) obj)._2())._2()).iterator().next();
							if (itr instanceof Integer) {
								type = "Integer";
							} else if (itr instanceof String) {
								type = "String";
							}
						} else {
							type = "null";
						}
						Iterator<?> ite = ((Iterable) ((Tuple2) ((Tuple2) obj)._2())._2()).iterator();
						switch (type) {
						case "Integer": {
							System.out.print((Integer) ite.next());
							while (ite.hasNext()) {
								System.out.print(",");
								System.out.print((Integer) ite.next());
							}
							break;
						}
						case "String": {
							System.out.print((String) ite.next());
							while (ite.hasNext()) {
								System.out.print(",");
								System.out.print((String) ite.next());
							}
							break;
						}
						case "null": {
							// do nothing
						}
						}
						System.out.print("]");
					} else {
						System.out.print("((Tuple2) ((Tuple2) obj)._2())._2() type not define");
					}
					System.out.println(")");
				} else {
					System.out.println("((Tuple2) obj)._2() type not define");
				}
			}
		}else{
			System.out.println("list.get[0] type not define");
		}
	}

	@Test
	public void test() {

		List<String[]> list0 = new ArrayList<String[]>() {
			{
				add(new String[] { "hehe", "haha" });
				add(new String[] { "aaa", "bbb" });
			}
		};

		PrintUtilPro.printList(list0);

		List<String> list1 = new ArrayList<String>() {
			{
				add("123456");
				add("678901");
				add("345678");
			}
		};
		PrintUtilPro.printList(list1);
	}

}
