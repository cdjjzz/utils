package ftps;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.concurrent.Callable;


public class Call implements Callable<Socket>{
	private Socket socket;
	
	private  int count=0;
	
	public  ThreadLocal<UserInfo> currtUser =new ThreadLocal<UserInfo>();  
	
	private InputStream inputStream;  
	
	public Call(Socket socket) {
		this.socket=socket;
	}
	
	@Override
	public Socket call() throws Exception {
		 System.out.println("hello");  
         
        try {  
        	  inputStream=socket.getInputStream();
              BufferedReader  reader = new BufferedReader(new InputStreamReader(inputStream));    
              Writer writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"));  
              while(true) {  
                  //第一次访问，输入流里面是没有东西的，所以会阻塞住  
                  if(count == 0)   
                  {  
                      writer.write("220");  
                      writer.write("\r\n");  
                      writer.flush();  
                      count++;  
                  }  
                  else {  
                      //两种情况会关闭连接：(1)quit命令 (2)密码错误  
                      if(!socket.isClosed()) {
	                    	 try {
	                             //进行命令的选择，然后进行处理，当客户端没有发送数据的时候，将会阻塞  
	                             String command = reader.readLine();    
	                             System.out.println(command);
	                             if(command !=null) {  
	                                 String[] datas = command.split(" ");  
	                                 Command commandSolver = CommandFactory.createCommand(datas[0]);   
	                                 if(commandSolver==null){
	                               	  writer.write("532");
	                               	  writer.flush();  
	                                 }else{
	                                 //登录验证,在没有登录的情况下，无法使用除了user,pass,quit之外的命令  
	                           	  String data = "";  
	                                 if(datas.length >=2) {  
	                                     data = datas[1];  
	                                 }  
	                                 if(commandSolver instanceof UserCommand 
	                               		  || commandSolver instanceof PassCommand
	                               		  || commandSolver instanceof QuitCommand
	                               		  || commandSolver instanceof UserManagerCommand) {  
	                                         commandSolver.getResult(data, writer,this);  
	                                     }else{  
	                                    if(isLogin()){
	                                   	 commandSolver.getResult(data, writer,this);  
	                                    }else{
	                                   	 currtUser.set(null);
	                                   	 writer.write("532");  
	                                        writer.flush();   
	                                    }
	                                 }  
	                         }  
	                         }  
	                       
						 } catch (Exception e) {
							
						}
                      }else{
                    	break;
                    }
                  }
              }
              
        } catch (IOException e) {  
            e.printStackTrace();  
        }    
        finally {  
             System.out.println("结束tcp连接");  
        }  
		return null;
	}
	
	public  boolean isLogin(){
	  UserInfo  userInfo=currtUser.get();
	  if(userInfo.isLogined()){
		  return true;
	  }
	  return false;
		
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	
	
	
	
}