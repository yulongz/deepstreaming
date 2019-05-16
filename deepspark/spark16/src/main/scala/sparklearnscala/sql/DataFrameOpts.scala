package sparklearnscala.sql

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

object DataFrameOpts {
  def main(args: Array[String]): Unit = {
    dfOpts;
  }
  def dfOpts {
    val conf = new SparkConf().setAppName(DataFrameOpts.getClass.getSimpleName()).setMaster("local");
    val sc = new SparkContext(conf);
    val sqlContext = new SQLContext(sc);

    val df = sqlContext.read.json("data/user.json") //.read.json("data/user.json");
    df.show();

    // Show the content of the DataFrame
    df.show();
    // age name
    // null Michael
    // 30 Andy
    // 19 Justin

    // Print the schema in a tree format
    df.printSchema();
    // root
    // |-- age: long (nullable = true)
    // |-- name: string (nullable = true)

    // Select only the "name" column
    df.select("name").show();
    // name
    // Michael
    // Andy
    // Justin

    // Select everybody, but increment the age by 1
    df.select(df.col("name"), df.col("age").plus(1)).show();
    // name (age + 1)
    // Michael null
    // Andy 31
    // Justin 20

    // Select people older than 21
    df.filter(df.col("age").gt(21)).show();
    // age name
    // 30 Andy

    // Count people by age
    df.groupBy("age").count().show();
    // age count
    // null 1
    // 19 1
    // 30 1
    println("============")
    df.collect().foreach(println)

  }
}