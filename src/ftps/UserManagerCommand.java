package ftps;

import java.io.Writer;
import java.util.List;

public class UserManagerCommand implements Command{

	@Override
	public void getResult(String data, Writer writer, Call t) {
		try {
			//�鿴��¼���û�
			StringBuffer sb=new StringBuffer();
			if("logined".equals(data)){
				sb.append("��ǰ��¼�û�:\r\n\t");
				List<UserInfo> infos=GlobaInfo.logined;
				for(UserInfo userInfo:infos){
					sb.append(userInfo.getName()+"\t"+userInfo.getLogin_time()+"\t"+userInfo.getPath()+"\r\n\t");
				}
			}else if("login".equals(data)){
				sb.append("��ǰδ��¼�û�:\r\n\t");
				List<UserInfo> infos=GlobaInfo.no_login;
				for(UserInfo userInfo:infos){
					sb.append(userInfo.getName()+"\r\n\t");
				}
			}else{
				sb.append("ϵͳȫ���û�:\r\n\t");
				List<UserInfo> infos=GlobaInfo.logined;
				for(UserInfo userInfo:infos){
					sb.append(userInfo.getName()+"\t"+userInfo.getLogin_time()+"\t"+userInfo.getPath()+"\t"+userInfo.isLogined()+"\r\n\t");
				}
				List<UserInfo> no_infos=GlobaInfo.no_login;
				for(UserInfo userInfo:no_infos){
					sb.append(userInfo.getName()+"\t"+userInfo.getLogin_time()+"\t"+userInfo.getPath()+"\t"+userInfo.isLogined()+"\r\n\t");
				}
			}
			sb.append("\r\n");
			writer.write(sb.toString());
			writer.flush();
		
		} catch (Exception e) {
		
		}
		
	}

}
