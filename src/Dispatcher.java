import java.io.*;
import java.net.*;
import java.util.*;

public class Dispatcher{
	static protected Set<ClientHandler> activeClients = new HashSet<ClientHandler>();
	public static void main(String[] args){
		try{
			ServerSocket sourceServer = new ServerSocket(7007);
			ServerSocket sinkServer = new ServerSocket(7008);
			Source source = new Source(sourceServer);
			Sink sink = new Sink(sinkServer);
			source.start();
			sink.start();
			
		} catch (IOException e){
			System.out.println(e);
		}
	}
}

class Source extends Thread {
	protected ServerSocket server;
	
	public Source(ServerSocket server){
		this.server = server;
	}
	public void run(){
	try{
		while (true){
			Socket in = server.accept();
			ClientHandler client = new ClientHandler(in, true);
			client.start();
		}
	} catch (IOException e){
		System.out.println(e);
	}
	}
}

class Sink extends Thread {
	protected ServerSocket server;
	
	public Sink(ServerSocket server){
		this.server = server;
	}
	public void run(){
	try{
		while (true){
			Socket in = server.accept();
			ClientHandler sink = new ClientHandler(in, false);
			Dispatcher.activeClients.add(sink);
			sink.start();
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
	protected boolean isSource;

	public synchronized void sendMessage(String msg){
		if (out != null){
			out.println(msg);
			out.flush();
		}
	}

	public ClientHandler(Socket socket, boolean isSource){
		this.socket = socket;
		this.isSource = isSource;
		
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
							if(isSource){
								Iterator<ClientHandler> iter = Dispatcher.activeClients.iterator();
								while (iter.hasNext()){
									ClientHandler t = iter.next();
									if (t != this) t.sendMessage(str);
								}
							}
						}
					}
				}
			socket.close();
			if(!isSource){
			Dispatcher.activeClients.remove(this);
			}
	} 
			catch (IOException e){
		System.out.println(e);
	}
	}
}
}
