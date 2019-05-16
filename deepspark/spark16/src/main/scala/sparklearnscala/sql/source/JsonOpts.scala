package sparklearnscala.sql.source

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

object JsonOpts {
  def main(args: Array[String]): Unit = {
    //jsonRead;
    jsonParallelize;
  }
  def jsonRead(): Unit = {
    val conf = new SparkConf().setAppName(JsonOpts.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    //DataFrame df = sqlContext.read().json("data/user.json");
    val df = sqlContext.read.format("json").load("data/user.json");
    df.show();
    //df.write().save("data/userparquet");
    //df.write().mode(SaveMode.Overwrite).save("data/userparquet");
  }

  def jsonParallelize(): Unit = {
    val conf = new SparkConf().setAppName(JsonOpts.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    // Alternatively, a DataFrame can be created for a JSON dataset represented by
    // an RDD[String] storing one JSON object per string.
    val jsonData = Array(
      "{\"name\":\"Yin\",\"address\":{\"city\":\"Columbus\",\"state\":\"Ohio\"}}");
    val anotherPeopleRDD = sc.parallelize(jsonData);
    val anotherPeople = sqlContext.read.json(anotherPeopleRDD);
    anotherPeople.show();
  }

}