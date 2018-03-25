package ftps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
/**
 * ∂¡»°≈‰÷√Œƒº˛
 * @author pet-lsf
 *
 */
public  class GlobaInfo {
	
	 public static String rootPath="D:/";
	 
	 public static List<UserInfo> logined=new ArrayList<UserInfo>();
	 
	 public static List<UserInfo> no_login=new ArrayList<UserInfo>();
	 
	 static{
		 Properties properties=new Properties();
		 try {
			properties.load(GlobaInfo.class.getResourceAsStream("/user.properties"));
			for(Entry<Object, Object> entry:properties.entrySet()){
				UserInfo userInfo=new UserInfo();
				userInfo.setName(String.valueOf(entry.getKey()));
				userInfo.setPassword(String.valueOf(entry.getValue()));
				userInfo.setPath(rootPath);
				no_login.add(userInfo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
	
}
