package ftps;

import java.io.File;
import java.io.Writer;

public class MkdCommand implements Command{

	@Override
	public void getResult(String data, Writer writer, Call t) {
		try {
			UserInfo userInfo=t.currtUser.get();
			String desDir = userInfo.getPath()+File.separator+data;
			File file = new File(desDir);
			if(!file.exists()){
				file.mkdirs();
			}
			writer.write("创建目录成功");
			writer.write("\r\n");
			writer.flush();
		} catch (Exception e) {
		}
		
	}

}
