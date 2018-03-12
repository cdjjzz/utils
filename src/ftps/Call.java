package ftps;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.concurrent.Callable;


public class Call implements Callable<Socket>{
	private Socket socket;
	
	private  int count=0;
	
	public  ThreadLocal<UserInfo> currtUser =new ThreadLocal<UserInfo>();  
	
	public Call(Socket socket) {
		this.socket=socket;
	}
	
	@Override
	public Socket call() throws Exception {
		 System.out.println("hello");  
         BufferedReader reader;  
        try {  
              reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));    
              Writer writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"));  
              while(true) {  
                  //��һ�η��ʣ�������������û�ж����ģ����Ի�����ס  
                  if(count == 0)   
                  {  
                      writer.write("220");  
                      writer.write("\r\n");  
                      writer.flush();  
                      count++;  
                  }  
                  else {  
                      //���������ر����ӣ�(1)quit���� (2)�������  
                      if(!socket.isClosed()) {
	                    	 try {
	                             //���������ѡ��Ȼ����д������ͻ���û�з������ݵ�ʱ�򣬽�������  
	                             String command = reader.readLine();    
	                             System.out.println(command);
	                             if(command !=null) {  
	                                 String[] datas = command.split(" ");  
	                                 Command commandSolver = CommandFactory.createCommand(datas[0]);   
	                                 if(commandSolver==null){
	                               	  writer.write("532 ��������\r\n");
	                               	  writer.flush();  
	                                 }else{
	                                 //��¼��֤,��û�е�¼������£��޷�ʹ�ó���user,pass,quit֮�������  
	                           	  String data = "";  
	                                 if(datas.length >=2) {  
	                                     data = datas[1];  
	                                 }  
	                                 if(commandSolver instanceof UserCommand 
	                               		  || commandSolver instanceof PassCommand
	                               		  || commandSolver instanceof QuitCommand) {  
	                                         commandSolver.getResult(data, writer,this);  
	                                     }else{  
	                                    if(isLogin()){
	                                   	 commandSolver.getResult(data, writer,this);  
	                                    }else{
	                                   	 currtUser.set(null);
	                                   	 writer.write("532 ִ�и�������Ҫ��¼�����¼����ִ����Ӧ�Ĳ���\r\n");  
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
             System.out.println("����tcp����");  
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
	
	
}