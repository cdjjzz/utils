package udpSocket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class BroadUdpClient{
	private static int port=8888;
	
	public static void main(String[] args) throws Exception {
		InetSocketAddress socketAddress=new InetSocketAddress("255.255.255.255", port);
		DatagramSocket datagramSocket=new DatagramSocket();
		byte[]  msg="ÄãºÃ°¡£¬ÎÒÊÇÂÞÊ¢·á".getBytes();
		DatagramPacket datagramPacket=new DatagramPacket(msg, msg.length,socketAddress);
		datagramSocket.send(datagramPacket);
		datagramSocket.receive(datagramPacket);
		System.out.println(new String(msg,0,msg.length));
		datagramSocket.close();
	}

}
