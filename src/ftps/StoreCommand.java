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
				//��������
				InputStream inSocket=t.getSocket().getInputStream();
				byte byteBuffer[] = new byte[1024]; 
				int amount; 
				//�����ֻ����������޷��ӿͻ�������������ȡ���ݣ�����Ϊ�ͻ���û�з�������ô
				while((amount =inSocket.read(byteBuffer) )!= -1){ 
					inFile.write(byteBuffer, 0, amount); 
				} 
				System.out.println("������ɣ��ر����ӡ�����");
				inFile.close();
				//�Ͽ���������
				
				writer.write("226 transfer complete\r\n"); 
				writer.flush();
			} 
			catch(IOException e){
				e.printStackTrace();
			} 
			
		
	
	}
}
