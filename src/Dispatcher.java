import java.io.*;
import java.net.*;
import java.util.*;

public class Dispatcher{
	static protected Set<ClientHandler> activeClients = new HashSet<ClientHandler>();
	public static void main(String[] args){
		try{
			ServerSocket server = new ServerSocket(7007);
			while (true){
				Socket in = server.accept();
				ClientHandler client = new ClientHandler(in);
				activeClients.add(client);
				client.start();
			}
		} catch (IOException e){
			System.out.println(e);
		}
	}
}

class ClientHandler extends Thread{
	protected Socket socket;
	protected BufferedReader in;
	protected PrintWriter out;

	public synchronized void sendMessage(String msg){
		if (out != null){
			out.println(msg);
			out.flush();
		}
	}

	public ClientHandler(Socket socket){
		this.socket = socket;
		try{
			if (socket != null){
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			}
		} 
		catch (IOException e){
			System.out.println(e);
		}
	}

	public void run() { 
		if (in != null && out!= null) {
			sendMessage("Write close to exit."); 
			try{
				while (true){
					String str = in.readLine(); 
					if (str == null){
						break;
					}
					else{
						if (str.trim().equals("close")){
							break; 
						} 
						else{
							Iterator<ClientHandler> iter = Dispatcher.activeClients.iterator();
							while (iter.hasNext()){
								ClientHandler t = iter.next();
								if (t != this) t.sendMessage(str);
							}
						}
					}
				}
			socket.close();
			Dispatcher.activeClients.remove(this);
	} 
			catch (IOException e){
		System.out.println(e);
	}
	}
}
}


