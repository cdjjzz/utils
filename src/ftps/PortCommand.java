package ftps;

import java.io.IOException;
import java.io.Writer;

public class PortCommand implements Command{

	@Override
	public void getResult(String data, Writer writer,Call t) {
		UserInfo userInfo=t.currtUser.get();
		String response = "200 the port an ip have been transfered";
		try {
			
			String[] iAp =  data.split(",");
			String ip = iAp[0]+"."+iAp[1]+"."+iAp[2]+"."+iAp[3];
			String port = Integer.toString(256*Integer.parseInt(iAp[4])+Integer.parseInt(iAp[5]));
			userInfo.setHost(ip);
			userInfo.setPort(Integer.valueOf(port));
			System.out.println("ip is "+ip);
			System.out.println("port is "+port);
			writer.write(response);
			writer.write("\r\n");
			writer.flush();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

}
