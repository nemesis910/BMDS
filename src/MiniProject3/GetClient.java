package MiniProject3;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class GetClient {

	private static int value;
	private static DatagramSocket destinationSocket = null;

	public static void main(String[] args) throws IOException,
			ClassNotFoundException {
		if (args.length != 3) {
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

		// create a DatagramSocket to sned
		destinationSocket = new DatagramSocket();

		// socket
		InetAddress address = InetAddress.getByName(args[0]);
		int port = Integer.parseInt(args[1]);

		// send the request for a specific message
		value = Integer.parseInt(args[2]);
		byte[] buff = new byte[512];

		// convert the value into byte array
		ByteBuffer b = ByteBuffer.allocate(4);
		buff = b.putInt(value).array();

		try {
			// getRequest
			DatagramPacket packet = new DatagramPacket(buff, buff.length,
					address, port);
			destinationSocket.send(packet);
			System.out.println("Request sent to ");

			// wait for response??
			packet = new DatagramPacket(buff, buff.length);
			destinationSocket.receive(packet);
			System.out.println("Message received");

			Message message = deserialize(buff);
			System.out.println(message.content);

		} finally {
			destinationSocket.close();
		}
	}

	public static Message deserialize(byte[] bytes) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = new ObjectInputStream(b);
		return (Message) o.readObject();

		// I hope this will work
	}

}
