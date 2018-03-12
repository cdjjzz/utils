package sockets;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import props.Infos;
import props.LocalString;
/**
 * 默认的行为是当底层网卡所有数据都发送完毕后，关闭连接通过setSoLinger方法，
 * 我们可以修改close方法的行为1，setSoLinger(true, 0)
 * 当网卡收到关闭连接请求后，无论数据是否发送完毕，立即发送RST包关闭连接
 */
public class SocketClient {
	private Socket socket;
	
	private  void init(){
		try {
			String host=Infos.getInfoByKey("host");
			int port=Infos.getInfoByKey(new LocalString("ftp_port"));
			socket=new Socket();//连接服务器
			socket.setSoTimeout(1000000000);
	//  	socket.setReceiveBufferSize(1000000000);
	//		socket.setTcpNoDelay(true);
			//socket.setSoLinger(false, 0);
			socket.connect(new InetSocketAddress(host, port),1000000000);
			readMsg();
			writeMsg();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public SocketClient() {
		init();
	}
	private void readMsg(){
	   //得到输出流
       new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
						InputStream inputStream=socket.getInputStream();
						byte b[]=new byte[1024];
						int len=0;
						while((len=inputStream.read(b))!=-1){
							System.out.println(new String(b,0,len));
						}
					} catch (IOException e) {
						e.printStackTrace();
						break;
					}
					
				 }
			}
	    	}).start();
      
	}
	private void  writeMsg(){
		   new Thread(new Runnable() {
			@Override
			public void run() {
				Scanner scanner=new Scanner(System.in);
				while(true){
					String text=scanner.nextLine();
					//得到输出流
					BufferedWriter bufferedWriter;
				 try {
					  bufferedWriter = new BufferedWriter(
							   new OutputStreamWriter(socket.getOutputStream()));
					  bufferedWriter.write(text+"\r\n");
					  bufferedWriter.flush();
					} catch (IOException e) {
					  e.printStackTrace();
					  break;
				  }
				 }
			}
	    	}).start();
		}
	public static void main(String[] args) {
		new SocketClient();
	}

}
