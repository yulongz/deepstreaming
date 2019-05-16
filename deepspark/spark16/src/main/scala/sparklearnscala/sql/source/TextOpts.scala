package sparklearnscala.sql.source

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

object TextOpts {
  
  case class Person(id: String,name:String, age: Int)
  
  def main(args: Array[String]): Unit = {
    textToDF;
  }
  
  def textToDF() = {
		val conf = new SparkConf().setAppName(TextOpts.getClass.getSimpleName()).setMaster("local");
		val sc = new SparkContext(conf);
		val sqlContext = new SQLContext(sc);


		val lines = sc.textFile("data/userinfo.txt");
  
		import sqlContext.implicits._
		val personsRDD = lines.map(_.split("\t")).map(p => Person(p(0), p(1) ,p(2).trim.toInt))
   val personsDF = personsRDD.toDF()
   
   personsDF.show();
		
		personsDF.registerTempTable("persons");
		
		val dataResults = sqlContext.sql("select * from persons where  age > 24");
		
		dataResults.show()

	}

  
}