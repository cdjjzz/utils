package ftps;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

/**
 * 改变工作目录
 * */
public class CwdCommand implements Command{

	@Override
	public void getResult(String data, Writer writer, Call t) {
		UserInfo userInfo=t.currtUser.get();
		String dir = userInfo.getPath();
		if(data.equals("..")){
			try {
				dir=dir.substring(0,dir.lastIndexOf("/"));
			} catch (Exception e) {
				dir=dir.substring(0,dir.lastIndexOf("\\"));
			}
		}else{
			dir+=File.separator+data;
		}
		File file = new File(dir);
		try {
			if((file.exists())&&(file.isDirectory())) {
				System.out.println(dir);
				userInfo.setPath(dir);
				writer.write("250 CWD command succesful");	
			}
			else 
			{
				writer.write("550 目录不存在");
			}
			writer.write("\r\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
