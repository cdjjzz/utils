package props;

import java.io.IOException;
import java.util.Properties;

public class Infos {
	private static  Properties properties=null;
	private Infos() {
	}
	
	static{
		properties=new Properties();
		try {
			/**
			 * getResourceAsStream /开始 读取类路径  非/开始读取当前class文件包路径
			 */
			properties.load(Infos.class.getResourceAsStream("/info.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
   public static Properties getProperties(){
		  return properties;
   }
   public static String  getInfoByKey(String key){
	   return properties.getProperty(key);
   }
   
   public static int getInfoByKey(CharSequence key){
	   return  Integer.parseInt((String) properties.get(key.toString()));
   }

}
