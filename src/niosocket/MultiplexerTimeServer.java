package niosocket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class MultiplexerTimeServer implements Runnable {
	
	   private Selector selector;
	   
	   private ServerSocketChannel serverSocketChannel;
	   
		
		public MultiplexerTimeServer(int port) throws Exception {
			selector=Selector.open();
			serverSocketChannel=ServerSocketChannel.open();
			//serverSocketChannel 注册到selector 允许客户端连接
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.bind(new InetSocketAddress(port));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} 
		@Override
		public void run() {
			while(true){
				try {
					//selector 注册的channel是否有事件通知  阻塞
					int  i=selector.select();
					System.out.println(i);
					//得到当前所有channel 事件通知 并开始处理对应的事件
					Set<SelectionKey> selectionKeys=selector.selectedKeys();
					Iterator<SelectionKey> iterator=selectionKeys.iterator();
					while(iterator.hasNext()){
						SelectionKey key=iterator.next();
						handInput(key);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
		}
		private void handInput(SelectionKey key) throws Exception {
			if(key.isValid()){
				if(key.isAcceptable()){
					SocketChannel socketChannel=((ServerSocketChannel)key.channel()).accept();
					socketChannel.configureBlocking(false);
					socketChannel.register(selector, SelectionKey.OP_READ);
				}else if(key.isReadable()){
					SocketChannel socketChannel=(SocketChannel)key.channel();
					ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
					//read >0 读取到数据 ==0 未读取到数据 ==-1 与远端peer断开连接。
					int read=socketChannel.read(byteBuffer);
					if(read>0){
						byteBuffer.flip();//将ByteBuffer从写模式调整至读模式
						byte[] b=new byte[byteBuffer.position()];
						byteBuffer.get(b);
						String text=new String(b ,Charset.forName("utf-8"));
						System.out.println(System.currentTimeMillis()+text);
						socketChannel.register(selector, SelectionKey.OP_WRITE|SelectionKey.OP_READ);
					}
				}else if(key.isWritable()){
					ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
					byteBuffer.put(("你好啊，当前时间："+System.currentTimeMillis()+"\r\n").getBytes("utf-8"));
					byteBuffer.flip();
					SocketChannel socketChannel=(SocketChannel)key.channel();
					socketChannel.write(byteBuffer);
					socketChannel.register(selector,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
				}
			}
			
		}
		public static void main(String[] args) {
			ByteBuffer buffer=ByteBuffer.allocate(1024);
			buffer.putInt(10);
			System.out.println(buffer.arrayOffset());
			System.out.println(buffer.position());
		}
}
