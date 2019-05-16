package sparklearnscala.sql.rddtodf

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{ StructType, StructField, StringType }
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types.IntegerType

object SpecifyingSchema {
  def main(args: Array[String]): Unit = {
    createDF;
  }

  def createDF {
    val conf = new SparkConf().setAppName(SpecifyingSchema.getClass.getSimpleName()).setMaster("local");
    val sc = new SparkContext(conf);
    val sqlContext = new SQLContext(sc);

    val lines = sc.textFile("data/userinfo.txt");

    // The schema is encoded in a string
    val schemaString = "id name age"

    // Import Row.

    // Import Spark SQL data types

    // Generate the schema based on the string of schema

    val StructFields = new Array[StructField](3)

    StructFields(0) = new StructField("id", StringType, true)
    StructFields(1) = new StructField("name", StringType, true)
    StructFields(2) = new StructField("age", IntegerType, true)

    val schema = StructType(StructFields)

    // Convert records of the RDD (people) to Rows.
    val rowRDD = lines.map(_.split("\t")).map(p => Row(p(0), p(1), p(2).trim.toInt))

    // Apply the schema to the RDD.
    val peopleDataFrame = sqlContext.createDataFrame(rowRDD, schema)

    // Register the DataFrames as a table.
    peopleDataFrame.registerTempTable("persons")

    // SQL statements can be run by using the sql methods provided by sqlContext.    
    val dataResults = sqlContext.sql("select * from persons where  age > 24");

    dataResults.show()

    // The results of SQL queries are DataFrames and support all the normal RDD operations.
    // The columns of a row in the result can be accessed by field index or by field name.
    dataResults.map(t => "Name: " + t(0)).collect().foreach(println)
  }
}