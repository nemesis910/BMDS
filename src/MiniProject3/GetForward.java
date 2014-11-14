package MiniProject3;

import java.net.*;

public class GetForward implements java.io.Serializable {
	int id;
	int port;
	InetAddress address;
	
	public GetForward(int id, InetAddress address, int port){
		this.id = id;
		this.address = address;
		this.port = port;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public InetAddress getAddress() {
		return address;
	}
	public void setAddress(InetAddress address) {
		this.address = address;
	}
	
}
