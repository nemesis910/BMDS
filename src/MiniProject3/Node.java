package MiniProject3;
import java.net.*;
import java.util.*;
import java.io.IOException;
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
			socket = new DatagramSocket(7007);
		}
		catch(IOException e){
		}
			}
	
	public Node(int port){
		this.port = port;
		try{
			socket = new DatagramSocket(7007);
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
	
	
}
