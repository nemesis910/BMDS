package MiniProject3;

import java.net.*;
import java.io.*;

public class PutClient {

	private static DatagramSocket destinationSocket = null;
	private static Message message;
	private static Resource res;

	public static void main(String[] args) throws IOException {

		if (args.length != 4) {
			System.out
					.println("Please input the hostname and port to which to connect + the identifier and the message you want to send");
			if (args[0] == "")
				System.out.println("Please input a hostname");
			if (args[1] == "")
				System.out.println("Please input the port on which to connect");
			if (args[2] == "")
				System.out.println("Please input the identifier");
			if (args[3] == "")
				System.out.println("Please input the message");

			return;
		}
		
		// Getting all the stuff from the command line
	    InetAddress address = InetAddress.getByName(args[0]);
		int port = Integer.parseInt(args[1]);
		int id = Integer.parseInt(args[2]);
		String resourceMessage = args[3];
		
			//DEBUG STUFF
			//InetAddress address = InetAddress.getByName("localhost");
			//int port = 7007;
			//int id = 2;
			//String resourceMessage = "boia!";
		
		// create a DatagramSocket to send it
		destinationSocket = new DatagramSocket();

		try {

			// create the buffer
			byte[] buff = new byte[512];

			// create the resource and the message
			res = new Resource(id, resourceMessage);
			message = new Message(Type.PUT, res);
			buff = Message.serialize(message);

			DatagramPacket packet = new DatagramPacket(buff, buff.length,
					address, port);
			destinationSocket.send(packet);
			
		} finally {
			destinationSocket.close();

		}

	}

}