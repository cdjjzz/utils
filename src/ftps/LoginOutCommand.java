package ftps;

import java.io.Writer;

public class LoginOutCommand implements Command {

	@Override
	public void getResult(String data, Writer writer, Call t) {
		try {
			UserInfo userInfo=t.currtUser.get();
			GlobaInfo.logined.remove(userInfo);
			userInfo.setLogin_time(0);
			userInfo.setLogined(false);
			userInfo.setPath(GlobaInfo.rootPath);
			GlobaInfo.no_login.add(userInfo);
			t.currtUser.set(null);
			writer.write("byebye£º"+userInfo.getName()+"\r\n");
			writer.flush();
		} catch (Exception e) {
		}
		
	}

}
