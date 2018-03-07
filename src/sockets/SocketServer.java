package sockets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;





import java.net.Socket;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import props.Infos;
import props.LocalString;

public class SocketServer {
	ServerSocket serverSocket=null;
	InetAddress inetAddress=null;
	InetSocketAddress inetSocketAddress=null;
	
	ExecutorService executorService=Executors.newFixedThreadPool(5);
	
	private SocketServer() {
		init();
	}
	public boolean init(){
		int port=Infos.getInfoByKey(new LocalString("port"));
		try {
			serverSocket=new ServerSocket(port);
			inetAddress=serverSocket.getInetAddress();
			inetSocketAddress=(InetSocketAddress) serverSocket.getLocalSocketAddress();
			while(true){
				Socket socket=bind();
				executorService.execute(new Runnable() {
					@Override
					public void run() {
						while(true){
							try {
								String msg=readMsg(socket);
								HttpURLConnection connection=initHttp();
								writeMsg(socket,getInfoFormTuling(connection,msg));
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
					}
				});
			}
		} catch (IOException e) {
			return false;
		}
		
	}
	public Socket bind(){
		try {
			Socket socket=serverSocket.accept();
			return socket;
		} catch (IOException e) {
			return null;
		}
	}
	public String readMsg(Socket socket){
		try {
			BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
			String msg=bufferedReader.readLine();
			return msg;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void writeMsg(Socket socket,String msg){
		try {
			BufferedWriter bufferedWriter=
					new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			bufferedWriter.write(msg+"\r\n");
			bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public HttpURLConnection initHttp() throws IOException{
		URL url=new URL("http://www.tuling123.com/openapi/api");
		HttpURLConnection connection=(HttpURLConnection)url.openConnection();
	    connection.setDoOutput(true);
	    connection.setDoInput(true);
	    connection.setRequestMethod("POST");
	    connection.setRequestProperty("Connection", "Keep-Alive");
	    //·¢ËÍjsonÊý¾Ý
	    connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
	    connection.setRequestProperty("accept","application/json");
	    return connection;
	}
	
	public String getInfoFormTuling(HttpURLConnection connection,String sendMsg) throws IOException{
		    String jsonStr="{\"key\":\"dbc331197a0d43f1b68671240b2e5b5a\",\"info\":\""+sendMsg+"\",\"userid\":\"224310\"}";
		    OutputStream outwritestream = connection.getOutputStream();
		    outwritestream.write(jsonStr.getBytes());
		    outwritestream.flush();
		    outwritestream.close();
		    String result=null;
         	BufferedReader reader = new BufferedReader(
                     new InputStreamReader(connection.getInputStream(),"utf-8"));
         	result= reader.readLine();
         	reader.close();
         	connection=null;
	        return result;
	}
	
	public static void main(String[] args) {
		new SocketServer();
	}
	
	
	
	

}
