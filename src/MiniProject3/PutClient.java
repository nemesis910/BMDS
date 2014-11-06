package MiniProject3;
import java.net.*;
import java.io.*;

public class PutClient {

	private static DatagramSocket destinationSocket=null;
	private static int port;
	

	public static void main(String[] args) throws IOException {
		
		if(args.length!=3)
		{
			System.out.println("Please input the hostname and port to which to connect + the message you want to send");
			return;
		}
		
			//create a DatagramSocket to send it
			destinationSocket = new DatagramSocket();
			
			//should we set the hostname to localhost??
			InetAddress address = InetAddress.getByName(args[0]);
			port=Integer.parseInt(args[1]);
			
			
			//we create a buffer in order to send a message
			//Message implements Serializable to be transform in an array of bytes
			Message message=new Message(Type.PUT,args[2] );
			byte[] buff =new byte[512]; // or max of a UDP Package			
			buff= serialize(message);
			
			
			
			DatagramPacket packet = new DatagramPacket(buff, buff.length,address,port);
			destinationSocket.send(packet);
			
			System.out.println(message.content);
			System.out.println("SENT!");
			
			
		
	}
	
	
	private static byte[] serialize(Message message) throws IOException
	{
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(message);
		return b.toByteArray();
	}
	

}
