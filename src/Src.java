import java.io.*;
import java.net.*;
import java.util.*;

public class Src{
	static protected Set<ClientHandler> activeClients = new HashSet<ClientHandler>();
	public static void main(String[] args){
		System.out.println("Establishing connection. Please wait ...");
		try{
			Socket socket = new Socket("localhost", 7007);
			SrcThread source = new SrcThread(socket);
			System.out.println("Connected: " + socket);
			source.start();
		} catch (IOException e){
			System.out.println(e);
		}
	}
}

class SrcThread extends Thread {
	protected Socket socket;

	public SrcThread(Socket socket){
		this.socket = socket;
	}
	public void run(){
		while (true){
			DataInputStream in = new DataInputStream(System.in);
			DataOutputStream out = null;
			try {
				out = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				System.out.println("Outputstream error: " + e.getMessage());
			}
			
			String line = "";
			try {
				line = in.readLine();
				out.writeUTF(line);
				out.flush();
			} catch(IOException e) {
				System.out.println("Sending error: " + e.getMessage());
			}
		}
	}
}