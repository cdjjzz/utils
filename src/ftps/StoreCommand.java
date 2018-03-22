package ftps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.net.Socket;



public class StoreCommand implements Command{

	@Override
	public void getResult(String data, Writer writer, Call t) {
			try{ 
				UserInfo userInfo=t.currtUser.get();
				writer.write("150 Binary data connection\r\n"); 
				writer.flush();
				FileOutputStream inFile = new 
						FileOutputStream(userInfo.getPath()+File.separator+data);
			    //数据连接  
                Socket tempSocket = new Socket(userInfo.getHost(),userInfo.getPort());   
                InputStream inSocket   
                = tempSocket.getInputStream();   
                byte byteBuffer[] = new byte[2048];   
                int amount;   
                //这里又会阻塞掉，无法从客户端输出流里面获取数据？是因为客户端没有发送数据么  
                while((amount =inSocket.read(byteBuffer) )!= -1){   
                    inFile.write(byteBuffer, 0, amount);   
                    inFile.flush();
                }   
                System.out.println("传输完成，关闭连接。。。");  
                inFile.close();  
                inSocket.close();  
                tempSocket.close();
				
				writer.write("226 transfer complete\r\n"); 
				writer.flush();
			} 
			catch(IOException e){
				e.printStackTrace();
			} 
			
		
	
	}
}
