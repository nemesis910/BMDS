package MiniProject3;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class GetClient {

	private static int value;
	private static DatagramSocket destinationSocket = null;

	public static void main(String[] args) throws IOException,
			ClassNotFoundException {
		if (args.length == 3) {
			System.out
					.println("Please input the hostname and port from where you want to get the message + the identifier of the message");

			if (args[0] == "")
				System.out.println("Input the host name");
			if (args[1] == "")
				System.out.println("Input the port");
			if (args[2] == "")
				System.out.println("Input the identifier value");

			return;
		}

		// get the stuff from the command line
		InetAddress address = InetAddress.getByName(args[0]);
		int port = Integer.parseInt(args[1]);
		int id = Integer.parseInt(args[2]);
		
		//DEBUG STUFF
		//InetAddress address = InetAddress.getByName("localhost");
		//int port = 7007;
		//int id = 1;
		
		// create a DatagramSocket to send
		destinationSocket = new DatagramSocket();

		
		try {

			// sendGet
			Message forward = new Message(Type.GET, id);
			byte[] buff = new byte[512];
			try{
			buff = Message.serialize(forward);
			DatagramPacket forwardPacket = new DatagramPacket(buff,buff.length, address, port);
			destinationSocket.send(forwardPacket);
			// sleep 3 seconds
			try {
				Thread.sleep(3000); // 3 seconds
				System.out.println("wait for 3 seconds");
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			// receive the message
			byte[] buff2 = new byte[512];
			DatagramPacket packet = new DatagramPacket(buff2, buff2.length);
			destinationSocket.receive(packet);
			System.out.println("Message received");

			Message message = Message.deserialize(buff2);
			
			System.out.println("Message is " + ((Resource)message.getContent()).getElement());

			System.out.println("It's done!");

		} finally {
			destinationSocket.close();
		}
	}
		catch(Exception e){
			
		}

}
}