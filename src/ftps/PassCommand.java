package ftps;

import java.io.IOException;
import java.io.Writer;

public class PassCommand implements Command{
	
	@Override
	public void getResult(String data, Writer writer,Call t) {
		
		System.out.println("execute the pass command");
		System.out.println("the data is "+data);
		//获得用户名
			UserInfo userInfo = t.currtUser.get();
	   try {
			if(userInfo!=null&&userInfo.getName()!=null){
				if(!userInfo.isLogined()){
					String response = null;
					if(userInfo.getPassword().equals(data)) {
						System.out.println("登录成功");
						userInfo.setLogin_time(System.currentTimeMillis());
						userInfo.setLogined(true);
						response = "230 User "+userInfo.getName()+" logged in";
						GlobaInfo.logined.add(userInfo);
						GlobaInfo.no_login.remove(userInfo);
					}
					else {
						System.out.println("登录失败，密码错误");
						response = "530   密码错误";
					}
						writer.write(response);
						writer.write("\r\n");
						writer.flush();
					
				}else{
					writer.write("当前用户已登录");
					writer.write("\r\n");
					writer.flush();
				}
				
			}else{
				writer.write("请先输入用户名后操作");
				writer.write("\r\n");
				writer.flush();
			}
	 	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
