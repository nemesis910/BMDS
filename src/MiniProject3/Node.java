package MiniProject3;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.io.*;

public class Node {

	int port;
	InetAddress neighborAddress;
	int neighborPort;
	Set<Socket> addressTable;
	Set<Resource> resourceTable;
	DatagramSocket socket;
	
	
	
	public Node(int port, String neighborAddress, int neighborPort){
		this.port = port;
		this.neighborPort = neighborPort;
		
		try{
			this.neighborAddress = InetAddress.getByName(neighborAddress);
		}
		catch(IOException e){
		}
		try{
			socket = new DatagramSocket(port);
		}
		catch(IOException e){
		}
		
		try {
			presentHimself();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Node(int port){
		
		
		this.port = port;
		try{
			socket = new DatagramSocket(port);
		}
		catch(IOException e){
		}
	}
	
	public void addNeighbor(Socket neighborSocket){
		addressTable.add(neighborSocket);
	}
	
	public void addResource(Resource resource){
		resourceTable.add(resource);
	}
	
	public boolean lookupResource(int id){
		boolean found = false;
		Iterator<Resource> iter = resourceTable.iterator();
		while(iter.hasNext()){
			Resource tmp = iter.next();
			if(tmp.getId()==id)
				found = true;
		}
		return found;
	}
	
	public void sendResource(int id, Socket destinationSocket){
		DatagramPacket packet;
		Iterator<Resource> iter = resourceTable.iterator();
		while(iter.hasNext()){
			Resource tmp = iter.next();
			if(tmp.getId()==id){
				try{
			      ByteArrayOutputStream baos = new ByteArrayOutputStream();
			      ObjectOutputStream oos = new ObjectOutputStream(baos);
			      oos.writeObject(tmp);
			      oos.flush();
			      // get the byte array of the object
			      byte[] buf= baos.toByteArray();
				
			      int number = buf.length;;
			      byte[] data = new byte[4];

			      // int -> byte[]
			      for (int i = 0; i < 4; ++i) {
			          int shift = i << 3; // i * 8
			          data[3-i] = (byte)((number & (0xff << shift)) >>> shift);
			      }
				
				packet = new DatagramPacket(data, 4, destinationSocket.getInetAddress(), destinationSocket.getPort());
				socket.send(packet);
				packet = new DatagramPacket(buf, buf.length, destinationSocket.getInetAddress(), destinationSocket.getPort());
				socket.send(packet);
				}
				catch(IOException e){
					
				}
				}
				
				
		}
	}
	
	public void presentHimself() throws IOException{
	
		System.out.println("presenting....");
		
			
			Message message = new Message(Type.PRESENTATION, port);
			byte[] buff = new byte[512]; // or max of a UDP Package
			buff = serialize(message);

			try {

				DatagramPacket packet = new DatagramPacket(buff, buff.length,
						neighborAddress, neighborPort);
				socket.send(packet);
				
				System.out.println("PRESENTED");

			} finally {


			}
		
	}
	
	

	public static void main(String[] args) {
		
		Node node;
		if (args.length == 1) {
			node = new Node(Integer.parseInt(args[0]));
		} else {
			node = new Node(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]));
		}
		while(true) {
			// to receive a message
            int MESSAGE_LEN = 1000;
            byte[] recvBuffer = new byte[MESSAGE_LEN];
            DatagramPacket packet = new DatagramPacket(recvBuffer,MESSAGE_LEN);
            
            System.out.println("Listening...");
			try {
				node.socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Message received");
			Message message = null;
			try {
			    message = deserialize(packet.getData());
				System.out.println(message);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	public static Message deserialize(byte[] bytes) throws IOException,
	ClassNotFoundException {
		Message h2;
	    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
	    ObjectInput in = new ObjectInputStream(bis);
	    h2 = (Message) in.readObject();
		return h2;
	}
	
	private static byte[] serialize(Message message) throws IOException {
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    ObjectOutput out = new ObjectOutputStream(bos);
	    out.writeObject(message);
	    byte b[] = bos.toByteArray();
	    out.close();
	    bos.close();

		return b;
	}
}
