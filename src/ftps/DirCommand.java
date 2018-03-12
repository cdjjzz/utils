
package ftps;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.UnknownHostException;

public class DirCommand implements Command{

	/**
	 * 获取ftp目录里面的文件列表
	 * */
	@Override
	public void getResult(String data, Writer writer,Call t) {
		UserInfo userInfo=t.currtUser.get();
		String desDir = userInfo.getPath()+File.separator+data;
		System.out.println(desDir);
		File dir = new File(desDir);
		if(!dir.exists()) {
			try {
				writer.write("210  文件目录不存在\r\n");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else 
		{
			StringBuilder dirs = new StringBuilder();
			System.out.println("文件目录如下：");
			dirs.append("文件目录如下:\r\n\t");
			String[] lists= dir.list();
			String flag = null;
			for(String name : lists) {
				System.out.println(name);
				File temp = new File(desDir+File.separator+name);
				if(temp.isDirectory()) {
					flag = "d";
				}
				else {
					flag = "f";
				}
				dirs.append(flag);
				dirs.append("  ");
				dirs.append(name);
				dirs.append("\r\n\t");
				
			}
			try {
				 writer.write("150 open ascii mode...\r\n");
				 writer.flush();
				 writer.write(dirs.toString());
				 writer.flush();
				 writer.write("220 transfer complete...\r\n");
				 writer.flush();
			} catch (NumberFormatException e) {
				
				e.printStackTrace();
			} catch (UnknownHostException e) {
			
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
	}

}

