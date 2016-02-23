import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Snk {
	public static void main(String[] args) throws UnknownHostException, IOException{
		@SuppressWarnings("resource")
		Socket sinkSocket = new Socket("localhost", 7008);   
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(sinkSocket.getInputStream()));   
		
		String message;
		
		while(true) {
			message = inFromServer.readLine();
			System.out.println(message);
		}
	}
}
