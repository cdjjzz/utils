package ftps;

import java.io.File;
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
				RandomAccessFile inFile = new 
				RandomAccessFile(userInfo.getPath()+File.separator+data,"rw");
				//数据连接
				InputStream inSocket=t.getSocket().getInputStream();
				byte byteBuffer[] = new byte[1024]; 
				int amount; 
				//这里又会阻塞掉，无法从客户端输出流里面获取数据？是因为客户端没有发送数据么
				while((amount =inSocket.read(byteBuffer) )!= -1){ 
					inFile.write(byteBuffer, 0, amount); 
				} 
				System.out.println("传输完成，关闭连接。。。");
				inFile.close();
				//断开数据连接
				
				writer.write("226 transfer complete\r\n"); 
				writer.flush();
			} 
			catch(IOException e){
				e.printStackTrace();
			} 
			
		
	
	}
}
