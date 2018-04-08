package udpSocket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class BroadUdpServer {
	
		private static int port=8888;
		
		public static void main(String[] args) throws Exception {
			DatagramSocket datagramSocket=new DatagramSocket(port);
			byte[]  recmsg=new byte[1024];
			DatagramPacket datagramPacket=new DatagramPacket(recmsg, recmsg.length);
			datagramSocket.receive(datagramPacket);
			System.out.println(new String(recmsg,0,datagramPacket.getLength()));
			String sendMsg="»¶Ó­£¡£¡£¡";
			datagramPacket.setData(sendMsg.getBytes());
			datagramSocket.send(datagramPacket);
			datagramSocket.close();
		}

}
