package sockets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import props.Infos;
import props.LocalString;

public class SocketClient {
	private Socket socket;
	
	private  void init(){
		try {
			String host=Infos.getInfoByKey("host");
			int port=Infos.getInfoByKey(new LocalString("port"));
			socket=new Socket(host, port);//连接服务器
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
				BufferedReader bufferedReader;
					try {
						bufferedReader = new BufferedReader(
								   new InputStreamReader(socket.getInputStream()));
						 String result=bufferedReader.readLine();
					     System.out.println(result+"\r\n");
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
