package MiniProject3;
import java.io.*;
import java.net.*;


public class GetClient {

	private int value;
	private static DatagramSocket destinationSocket=null;
	
	public static void main(String[] args) throws IOException
	{
		if(args.length!=2)
		{
			System.out.println("Please input the hostname and port from where you want to get the message");
			return;
		}
		
		//create a DatagramSocket to sned
		destinationSocket = new DatagramSocket();
		
		
		
	}
	
}
