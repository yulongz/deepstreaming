/** 
 * Project Name:sparklearn 
 * File Name:SchemaUsingReflection.java 
 * Package Name:sparklearn.sql.rddtodf 
 * Date:2017年7月12日下午4:47:04 
 * sky.zyl@hotmail.com
*/

package sparklearn.sql.rddtodf;

import java.io.Serializable;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import sparklearn.sql.source.JsonOpts;
import sparklearn.util.PrintUtilPro;

/**
 * ClassName:SchemaUsingReflection <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年7月12日 下午4:47:04 <br/>
 * 
 * @author hadoop
 * @version
 * @see
 */
public class SchemaUsingReflection {

	public static void main(String[] args) {
		createDF();
	}

	public static void createDF() {
		// sc is an existing JavaSparkContext.
		SparkConf conf = new SparkConf().setAppName(SchemaUsingReflection.class.getSimpleName()).setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		SQLContext sqlContext = new org.apache.spark.sql.SQLContext(sc);

		// Load a text file and convert each line to a JavaBean.
		JavaRDD<Person> people = sc.textFile("data/userinfo.txt").map(new Function<String, Person>() {
			public Person call(String line) throws Exception {
				String[] parts = line.split("\t");
				Person person = new Person();
				person.setName(parts[1]);
				person.setAge(Integer.parseInt(parts[2].trim()));
				return person;
			}
		});

		// Apply a schema to an RDD of JavaBeans and register it as a table.
		DataFrame schemaPeople = sqlContext.createDataFrame(people, Person.class);
		schemaPeople.registerTempTable("people");

		// SQL can be run over RDDs that have been registered as tables.
		DataFrame teenagers = sqlContext.sql("SELECT name FROM people WHERE age >= 24 AND age <= 29");

		// The results of SQL queries are DataFrames and support all the normal
		// RDD operations.
		// The columns of a row in the result can be accessed by ordinal.
		List<String> teenagerNames = teenagers.javaRDD().map(new Function<Row, String>() {
			public String call(Row row) {
				return "Name: " + row.getString(0);
			}
		}).collect();
		PrintUtilPro.printList(teenagerNames);
	}

	public static class Person implements Serializable {
		private String name;
		private int age;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
	}
}
