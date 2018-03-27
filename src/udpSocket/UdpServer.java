package udpSocket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class UdpServer {
	public static void main(String[] args) throws Exception {
		DatagramSocket datagramSocket=new DatagramSocket(9600);
		DatagramPacket datagramPacket=new DatagramPacket(new byte[1024], 1024);
		while(true){
			datagramSocket.receive(datagramPacket);
		    byte[] receiveMsg = Arrays.copyOfRange(datagramPacket.getData(),
		    		datagramPacket.getOffset(),
		    		datagramPacket.getOffset() + datagramPacket.getLength());
 
            System.out.println("Handing at client "
                    + datagramPacket.getAddress().getHostName() + " ip "
                    + datagramPacket.getAddress().getHostAddress());
 
            System.out.println("Server Receive Data:" +ThreadLocalRandom.current().nextDouble()+ new String(receiveMsg));
            datagramPacket.setData("ÄãÊÇsb".getBytes());
            datagramSocket.send(datagramPacket);
		}
	}

}
