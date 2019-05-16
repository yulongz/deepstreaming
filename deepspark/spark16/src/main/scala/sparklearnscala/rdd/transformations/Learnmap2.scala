package sparklearnscala.rdd.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import com.google.gson.Gson

object Learnmap2 {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName(Learnmap2.getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)

    val datas: Array[String] = Array(
      "{'id':1,'name':'xl1','pwd':'xl123','sex':2}",
      "{'id':2,'name':'xl2','pwd':'xl123','sex':1}",
      "{'id':3,'name':'xl3','pwd':'xl123','sex':2}")

    sc.parallelize(datas)
      .map(v => {
        new Gson().fromJson(v, classOf[User])
      })
      .foreach(user => {
        println("id: " + user.id
          + " name: " + user.name
          + " pwd: " + user.pwd
          + " sex:" + user.sex)
      })
  }
}
case class User(var id: String,var name: String,var pwd: String,var sex: String) {

}
//class User {
//  var id = ""
//  var name = ""
//  var pwd = ""
//  var sex = ""
//
//  def getId(): String = {
//    return this.id;
//  }
//  def setId(id: String): Unit = {
//    this.id = id;
//  }
//  def getName(): String = {
//    return this.name;
//  }
//  def setName(name: String): Unit = {
//    this.name = name;
//  }
//  def getPwd(): String = {
//    return this.pwd;
//  }
//  def setPwd(pwd: String): Unit = {
//    this.pwd = pwd;
//  }
//  def getSex(): String = {
//    return this.sex;
//  }
//  def setSex(id: String): Unit = {
//    this.sex = sex;
//  }
//
//}