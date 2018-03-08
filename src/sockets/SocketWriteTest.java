package sockets;

 

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;

import javax.print.DocFlavor.URL;

import org.junit.Test;

 

public class SocketWriteTest {

    public static final int PORT = 12123;

    public static final int BUFFER_SIZE = 1024;

    //����˴���

    @Test

    public void server() throws IOException, InterruptedException{

       ServerSocket ss = new ServerSocket(PORT);

       while(true)

       {
           Socket s = ss.accept();
           s.setSendBufferSize(4);
           //�����������������д��
           s.getOutputStream().write("��� ".getBytes());
           s.getOutputStream().flush();

           s.getOutputStream().write("�ǳ��� ".getBytes());

           s.getOutputStream().flush();

         //  s.close();

       }

    }

   

    //�ͻ��˴���

    @Test
    public void client() throws UnknownHostException,Exception{
    	URI uri=new URI("http://www.baidu.com");
    	System.out.println(uri.getHost());
    	System.out.println(uri.getPort());
    /**
     * socket ��д״̬:socket.accpet(),������д��һֱ����������SendBufferSize�����
     * socket �ɶ�״̬:socket.accpet(),�ȴ��Է�д�뵽ReceiveBufferSize���У���δ���������
     * �������ӵ���һ�����׽��ֵ�ͨ��д�����ݺ�
     * ���׽��ַ��ܴ��ڿɶ�״̬��ע�⣬����Է��׽����Ѿ��رգ���ô�����׽��ֽ����ڿɶ�״̬������ÿ�ε���read�󣬷��صĶ���-1����
     * ���ڿɶ�״̬���ڶԷ�д�����ݺ��socket�ر�ʱ���ܳ��֣��������ͻ��˺ͷ���˶�ͣ����readʱ��
     * ���û���κ�һ������Է�д�����ݣ��⽫�����һ��������
     */
//       byte[] buffer;
//       Socket socket= new Socket("localhost",PORT);//����socket����
//       socket.getReceiveBufferSize();
////       System.out.println(socket.getSendBufferSize());
////
////       System.out.println(socket.getReceiveBufferSize());
//       socket.getOutputStream().write(new byte[BUFFER_SIZE]);
//       socket.getOutputStream().flush();
//       int i = socket.getInputStream().read(buffer = new byte[BUFFER_SIZE]);
//       System.out.println(new String(buffer,0,i));
//       socket.getOutputStream().write(new byte[BUFFER_SIZE]);
//       socket.getOutputStream().flush();
//       i = socket.getInputStream().read(buffer = new byte[BUFFER_SIZE]);
//       System.out.println(new String(buffer,0,i));
    }

}