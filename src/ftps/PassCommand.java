package ftps;

import java.io.IOException;
import java.io.Writer;

public class PassCommand implements Command{
	
	@Override
	public void getResult(String data, Writer writer,Call t) {
		
		System.out.println("execute the pass command");
		System.out.println("the data is "+data);
		//����û���
			UserInfo userInfo = t.currtUser.get();
	   try {
			if(userInfo!=null&&userInfo.getName()!=null){
				if(!userInfo.isLogined()){
					String response = null;
					if(userInfo.getPassword().equals(data)) {
						System.out.println("��¼�ɹ�");
						userInfo.setLogin_time(System.currentTimeMillis());
						userInfo.setLogined(true);
						response = "230 User "+userInfo.getName()+" logged in";
						GlobaInfo.logined.add(userInfo);
						GlobaInfo.no_login.remove(userInfo);
					}
					else {
						System.out.println("��¼ʧ�ܣ��������");
						response = "530   �������";
					}
						writer.write(response);
						writer.write("\r\n");
						writer.flush();
					
				}else{
					writer.write("��ǰ�û��ѵ�¼");
					writer.write("\r\n");
					writer.flush();
				}
				
			}else{
				writer.write("���������û��������");
				writer.write("\r\n");
				writer.flush();
			}
	 	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
