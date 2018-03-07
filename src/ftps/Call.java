package ftps;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.concurrent.Callable;

import javax.xml.ws.handler.MessageContext.Scope;

public class Call implements Callable<Socket>{
	private Socket socket;
	
	private  int count=0;
	
	public static ThreadLocal<UserInfo> currtUser =new ThreadLocal<UserInfo>();  
	
	public Call(Socket socket) {
		this.socket=socket;
	}
	
	@Override
	public Socket call() throws Exception {
		 System.out.println("hello");  
         BufferedReader reader;  
        try {  
              reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));    
              Writer writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));  
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
                          //���������ѡ��Ȼ����д������ͻ���û�з������ݵ�ʱ�򣬽�������  
                          String command = reader.readLine();    
                          if(command !=null) {  
                              String[] datas = command.split(" ");  
                              Command commandSolver = CommandFactory.createCommand(datas[0]);   
                              //��¼��֤,��û�е�¼������£��޷�ʹ�ó���user,pass֮�������  
                              if(commandSolver instanceof UserCommand) {  
                                      String data = "";  
                                      if(datas.length >=2) {  
                                          data = datas[1];  
                                      }  
                                      commandSolver.getResult(data, writer,this);  
                                  }  
                              }else{  
                                  writer.write("532 ִ�и�������Ҫ��¼�����¼����ִ����Ӧ�Ĳ���\r\n");  
                                  writer.flush();  
                              }  
                          } else {  
                          //�����Ѿ��رգ�����̲߳����д��ڵı�Ҫ  
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
}