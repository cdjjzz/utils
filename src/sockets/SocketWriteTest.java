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

    //服务端代码

    @Test

    public void server() throws IOException, InterruptedException{

       ServerSocket ss = new ServerSocket(PORT);

       while(true)

       {
           Socket s = ss.accept();
           s.setSendBufferSize(4);
           //这里向网络进行两次写入
           s.getOutputStream().write("你好 ".getBytes());
           s.getOutputStream().flush();

           s.getOutputStream().write("非常好 ".getBytes());

           s.getOutputStream().flush();

         //  s.close();

       }

    }

   

    //客户端代码

    @Test
    public void client() throws UnknownHostException,Exception{
    	URI uri=new URI("http://www.baidu.com");
    	System.out.println(uri.getHost());
    	System.out.println(uri.getPort());
    /**
     * socket 可写状态:socket.accpet(),即可书写，一直到数据填满SendBufferSize缓冲池
     * socket 可读状态:socket.accpet(),等待对方写入到ReceiveBufferSize池中，并未处理的数据
     * 仅当连接的另一方向本套接字的通道写入数据后，
     * 本套接字方能处于可读状态（注意，如果对方套接字已经关闭，那么本地套接字将处于可读状态，并且每次调用read后，返回的都是-1）。
     * 由于可读状态是在对方写入数据后或socket关闭时才能出现，因此如果客户端和服务端都停留在read时，
     * 如果没有任何一方，向对方写入数据，这将会产生一个死锁。
     */
//       byte[] buffer;
//       Socket socket= new Socket("localhost",PORT);//创建socket连接
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