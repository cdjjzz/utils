package ftps;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

/**
 * 处理文件的发送
 * */
public class GetCommand implements Command{

	@Override
	public void getResult(String data, Writer writer, Call t) {
		UserInfo userInfo=t.currtUser.get();
		String desDir = userInfo.getPath()+File.separator+data;
		File file = new File(desDir);
		System.out.println(desDir);
		if(file.exists())
		{
			try {
				 writer.write("150 open ascii mode...\r\n");
				 writer.flush();
				 InputStream is = new FileInputStream(file); 
				 byte[] buf = new byte[1024];
				 int len=0;
				 while((len= is.read(buf))!=-1) {
					 writer.write(new String(buf,0,len)+"\r\n");
					 writer.flush();
				 }
				 writer.write("220");
				 writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			try {
				writer.write("220  该文件不存在\r\n");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
