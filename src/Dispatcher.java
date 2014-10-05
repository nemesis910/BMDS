import java.io.*;
import java.net.*;
import java.util.*;

public class Dispatcher{
	static protected Set<ClientHandler> activeClients = new HashSet<ClientHandler>();
	public static void main(String[] args){
		try{
			// It just initialize the two server that will handle sink and source request and pass them to the two correspondant thread
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
			//It accept the connection, it pass it to a handler tread and with the variable true it says that he's a source process
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
			//Like in source thread (they are almost the same) just set the boolean variable to false 
			//(cause it is sink and not source) and it add a reference to this connection to a set 
			//that contains all the references to sink processes
			Socket in = server.accept();
			ClientHandler sink = new ClientHandler(in, false);
			Src.activeClients.add(sink);
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
					//it reads a string from the process
					String str = in.readLine(); 
					if (str == null){
						break;
					}
					else{
						//if the string is "close" he closes the connection with this thread
						if (str.trim().equals("close")){
							break; 
						} 
						else{
							//if the process is a source (boolean variable isSource setted true) it send the message received 
							//to all the process contained in the sink set
							if(isSource){
								System.out.println(str);
								Iterator<ClientHandler> iter = Src.activeClients.iterator();
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
			Src.activeClients.remove(this);
			}
	} 
			catch (IOException e){
		System.out.println(e);
	}
	}
}
}
