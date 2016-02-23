package MiniProject3;

import java.net.*;

public class GetForward implements java.io.Serializable {
	int id;
	int port;
	InetAddress address;
	int ttl;
	
	public GetForward(int id, InetAddress address, int port, int ttl){
		this.id = id;
		this.address = address;
		this.port = port;
		this.ttl = ttl;
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
	public int getTtl() {
		return ttl;
	}
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}
}
