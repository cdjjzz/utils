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
			 * getResourceAsStream /��ʼ ��ȡ��·��  ��/��ʼ��ȡ��ǰclass�ļ���·��
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
