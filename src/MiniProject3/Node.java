package MiniProject3;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.io.*;

public class Node {

	int port;
	InetAddress neighborAddress;
	int neighborPort;
	static Set<SocketAddress> addressTable;
	static Set<Resource> resourceTable;
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
	
	public void addNeighbor(SocketAddress neighborSocket){
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
	
	public void sendResource(int id, int destinationPort, InetAddress destinationAddress){
		DatagramPacket packet;
		Iterator<Resource> iter = resourceTable.iterator();
		while(iter.hasNext()){
			Resource tmp = iter.next();
			if(tmp.getId()==id){
				try{
			      Message message = new Message(Type.GET, tmp);
			      byte[] buff = new byte[512]; // or max of a UDP Package
				  buff = serialize(message);
				
				packet = new DatagramPacket(buff, buff.length, destinationAddress, destinationPort);
				socket.send(packet);
				}
				catch(IOException e){
					
				}
				}
				
				
		}
	}
	
	public void forwardGet(int id, InetAddress address, int port) throws IOException{
		
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
		resourceTable = new HashSet<Resource>();
		addressTable = new HashSet<SocketAddress>();
		
		if (args.length == 1) {
			node = new Node(Integer.parseInt(args[0]));
		} 
		if (args.length == 3){
			node = new Node(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]));
		}
		else 
			node = new Node(7007);
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
			Message message = new Message();
			try {
			    message = deserialize(packet.getData());
				System.out.println(message);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			
			if(message.getType()==Type.PUT){
				node.addResource((Resource)message.content);
				System.out.println("Resource saved, message was " + ((Resource) message.content).getElement() );
			}
			if(message.getType()==Type.GET){
				if(node.lookupResource((Integer)message.content)){
					System.out.print("FOUND");
					node.sendResource((Integer)message.content, packet.getPort(), packet.getAddress());
				}
				else{
					for(SocketAddress s:addressTable){
						InetSocketAddress receiver = (InetSocketAddress)s;
						Message forward = new Message(Type.GETFORWARD, new GetForward((Integer)message.content, packet.getAddress(), packet.getPort()));
						byte[] buff = new byte[512];
						try{
						buff = serialize(forward);
						DatagramPacket forwardPacket = new DatagramPacket(buff,buff.length, receiver.getAddress(), receiver.getPort());
						node.socket.send(forwardPacket);
						}
						catch(Exception e){
							
						}
					}
				}
			}
			
			if(message.getType()==Type.GETFORWARD){
				if(node.lookupResource(((GetForward) message.content).getId())){
					node.sendResource(((GetForward) message.content).getId(), ((GetForward) message.content).getPort(), ((GetForward) message.content).getAddress());
				}
				else{
					for(SocketAddress s:addressTable){
						InetSocketAddress receiver = (InetSocketAddress)s;
						Message forward = new Message(Type.GETFORWARD, new GetForward((Integer)message.content, ((GetForward) message.content).getAddress(), ((GetForward) message.content).getPort()));
						byte[] buff = new byte[512];
						try{
						buff = serialize(forward);
						DatagramPacket forwardPacket = new DatagramPacket(buff,buff.length, receiver.getAddress(), receiver.getPort());
						node.socket.send(forwardPacket);
						}
						catch(Exception e){
							
						}
					}
				}
			}
			if(message.getType()==Type.PRESENTATION){
				node.addNeighbor(packet.getSocketAddress());
	
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
