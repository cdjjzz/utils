package niosocket;

public class TimeServer {
	public static void main(String[] args) throws Exception {
		int port=12345;
		MultiplexerTimeServer  server =new MultiplexerTimeServer(port);
		new Thread(server).start();
	}

}
