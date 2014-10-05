import java.io.*; 
import java.net.*; 

class Src { 
	public static void main(String argv[]) throws Exception  { 
		
		String message;   
		String welcomeToServer;   
		
		BufferedReader console = new BufferedReader( new InputStreamReader(System.in));   
		Socket clientSocket = new Socket("localhost", 7007);   
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());   
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));   
		
		welcomeToServer = inFromServer.readLine();   
		System.out.println(welcomeToServer);
		
		message = console.readLine(); 
		
		while(!message.equalsIgnoreCase("close")){
			outToServer.writeBytes(message + '\n');   
			message = console.readLine(); 
		}  
		clientSocket.close();  
		}
	}