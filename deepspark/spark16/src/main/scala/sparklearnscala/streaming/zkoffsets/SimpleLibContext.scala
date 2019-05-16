package sparklearnscala.streaming.zkoffsets

import com.typesafe.config.ConfigFactory
import scala.collection.JavaConverters._ //隐式转换，这样才能有asScala这个方法
import com.typesafe.config.Config
import java.util.Map.Entry

object SimpleLibContext {
  def main(args: Array[String]): Unit = {
    val s = new SimpleLibContext();
    //s.printAllSettings();
    s.printAllProperties;
  }
}

class SimpleLibContext {

  var config = ConfigFactory.load;

  // 指定配置文件
  def SimpleLibContext(config: Config): Unit = {
    this.config = config;
    config.checkValid(ConfigFactory.defaultReference(), "simple-lib");
  }

  // 打印
  def printSetting(path: String): Unit = {
    println("The setting '" + path + "' is: " + config.getString(path));
  }

  def printAllProperties(): Unit = {
    val set = config.entrySet();
    val iter = set.iterator()
    while (iter.hasNext) {
      val str = iter.next;
      println(str.getKey + ":" + str.getValue.unwrapped());
    }
  }

  def printAllSettings() {
    val list = config.getStringList("simple-app").asScala; //必须使用List结构才能读取，如下：
    //simple-app=["answer=42","question=40+2"]
    list.foreach(println)
  }

}