package ftps;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import props.Infos;
import props.LocalString;

public class FTPServer {
	ServerSocket serverSocket;
	ExecutorService executorService=Executors.newFixedThreadPool(5);
	
	
	public FTPServer() {
		init();
		while(true){
			try {
				Socket socket=serverSocket.accept();
				executorService.submit(new  Call(socket));
			} catch (Exception e) {
			}
			
		}
	}
	
    private void init(){
    	try {
    		int port =Infos.getInfoByKey(new LocalString("ftp_port"));
    		serverSocket=new ServerSocket(port);
			serverSocket.setSoTimeout(1000000000);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public static void main(String[] args) {
		new FTPServer();
	}
	

}


