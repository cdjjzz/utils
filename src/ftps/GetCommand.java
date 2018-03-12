package ftps;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

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
				 OutputStream outputStream=t.getSocket().getOutputStream();
				 while((len= is.read(buf))!=-1) {
					 outputStream.write(buf,0,len);
					 outputStream.flush();
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
