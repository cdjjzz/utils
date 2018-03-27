package udpSocket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;
/**
 * udpµ¥²¥
 * @author pet-lsf
 *
 */
public class UdpClient {
	public static void main(String[] args) throws Exception {
		byte[] msg = new String("connect test successfully!!!").getBytes();
		 
        DatagramSocket client = new DatagramSocket();
 
        SocketAddress socketAddr =new InetSocketAddress("127.0.0.1",9600);
 
        DatagramPacket sendPacket = new DatagramPacket(msg, msg.length,
                socketAddr);
 
        client.send(sendPacket);
        client.receive(sendPacket);
	    byte[] receiveMsg = Arrays.copyOfRange(sendPacket.getData(),
	    		sendPacket.getOffset(),
	    		sendPacket.getOffset() + sendPacket.getLength());
	    System.out.println(new String(receiveMsg));
        client.close();
	}

}
