import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private ServerSocket serverSocket;
	private Socket acceptedSocket;
	private BufferedReader input;
	private PrintStream output;
	
	public static void main(String[] args) {
		Server server = new Server();
		server.run();
	}
	
	public void run() {
		try {
			serverSocket = new ServerSocket(9999, 10); //maximo de 10 clients
			acceptedSocket = serverSocket.accept();
			
			output = new PrintStream(acceptedSocket.getOutputStream());
			input = new BufferedReader(new InputStreamReader(acceptedSocket.getInputStream()));
			
			String message = input.readLine();
			System.out.println(message);
			
			if(message !=  null) {
				output.println("Welcomes");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}
