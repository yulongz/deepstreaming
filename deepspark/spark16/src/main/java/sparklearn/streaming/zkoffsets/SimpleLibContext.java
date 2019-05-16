/** 
 * Project Name:sparklearn 
 * File Name:SimpleLibContext.java 
 * Package Name:sparklearn.streaming.zkoffsets 
 * Date:2017年7月18日下午4:30:13 
 * sky.zyl@hotmail.com
*/  
  
package sparklearn.streaming.zkoffsets;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

/** 
 * ClassName:SimpleLibContext <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年7月18日 下午4:30:13 <br/> 
 * @author   hadoop 
 * @version   
 * @see       
 */
public class SimpleLibContext {

	private Config config;

	// 指定配置文件
	public SimpleLibContext(Config config) {
		this.config = config;
		config.checkValid(ConfigFactory.defaultReference(), "simple-lib");
	}

	// 默认加载classpath下的application.*
	public SimpleLibContext() {
		this(ConfigFactory.load());
	}

	// 打印
	public void printSetting(String path) {
		System.out.println("The setting '" + path + "' is: " + config.getString(path));
	}
	
	public void printAllProperties(){
		 Set<Map.Entry<String, ConfigValue>> set = config.entrySet();		 
		 for (Iterator<Entry<String, ConfigValue>> iter = set.iterator(); iter.hasNext();) {
			  Entry<String, ConfigValue> str = (Map.Entry<String, ConfigValue>) iter.next();
			  System.out.println(str.getKey()+":"+str.getValue().unwrapped());
			 }
	}
	
	public void printAllSettings(){
		List<String> list = config.getStringList("simple-app");//必须使用List结构才能读取，如下：
		//simple-app=["answer=42","question=40+2"]
		for(String str:list){
			System.out.println(str);
		}
	}

	public static void main(String[] args) {
		SimpleLibContext s = new SimpleLibContext();
		//s.printSetting("simple-app.answer");
		s.printAllSettings();
		
	}
}
 