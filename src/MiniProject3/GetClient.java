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

		// get the stuff from the command line
		InetAddress address = InetAddress.getByName(args[0]);
		int port = Integer.parseInt(args[1]);
		int id = Integer.parseInt(args[2]);

		// create a DatagramSocket to send
		destinationSocket = new DatagramSocket();

		
		try {

			// sendGet
			Message.sendGet(id, address, port);
			// sleep 3 seconds
			/*try {
				Thread.sleep(3000); // 3 seconds
				System.out.println("wait for 3 seconds");
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
*/
			// receive the message
			byte[] buff = new byte[512];
			DatagramPacket packet = new DatagramPacket(buff, buff.length);
			destinationSocket.receive(packet);
			System.out.println("Message received");

			Message message = Message.deserialize(buff);

			System.out.println("It's done!");

		} finally {
			destinationSocket.close();
		}
	}

}