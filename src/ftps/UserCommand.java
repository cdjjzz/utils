package ftps;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public class UserCommand implements Command{

	@Override
	public void getResult(String data, Writer writer, Call t) {
		Iterator<UserInfo> userInfos=GlobaInfo.logined.iterator();
		String response="";
		while(userInfos.hasNext()){
			UserInfo  userInfo=userInfos.next();
			if(userInfo.getName().equals(data)){
				 t.currtUser.set(userInfo);
				 write(writer, "331 请输入密码");
				 return;
			}else{
				response="501 不存在该用户";
			}
		}
		Iterator<UserInfo> userInfs=GlobaInfo.no_login.iterator();
		while(userInfs.hasNext()){
			UserInfo  userInfo=userInfs.next();
			if(userInfo.getName().equals(data)){
				 t.currtUser.set(userInfo);
				 write(writer, "331 请输入密码");
				 return;
			}else{
				response="501 不存在该用户";
			}
		}
		write(writer, response);
	}
	private  void write(Writer writer,String response){
		 try {
			writer.write(response);
			 writer.write("\r\n");  
	         writer.flush();  
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}

}
