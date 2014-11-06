package MiniProject3;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;


public class GetClient {

	private static int value;
	private static DatagramSocket destinationSocket=null;
	
	public static void main(String[] args) throws IOException
	{
		if(args.length!=2)
		{
			System.out.println("Please input the hostname and port from where you want to get the message + the identifier of the message");
			return;
		}
		
		//create a DatagramSocket to sned
		destinationSocket = new DatagramSocket();
		
		//socket
		InetAddress address = InetAddress.getByName(args[0]);
		int port = Integer.parseInt(args[1]);
		
		//send the request for a specific message
		value = Integer.parseInt(args[2]);
		byte[] buff = new byte[512];
		
		//convert the value into byte array
		ByteBuffer b =ByteBuffer.allocate(4);
		buff = b.putInt(value).array();
		
		
		//getRequest
		DatagramPacket packet = new DatagramPacket(buff, buff.length,address,port);
		destinationSocket.send(packet);
		
		////we need a second meeting :))
		
		
	}
	
	

	
	
}
