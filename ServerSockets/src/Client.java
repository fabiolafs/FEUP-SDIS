import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Client {

	private Socket clientSocket;
	private BufferedReader input;
	private PrintStream output;
	
	public static void main(String[] args) {
		Client client = new Client();
		client.run();
	}

	public void run()  {
		try {
			clientSocket = new Socket("localhost", 9999);
		
			output = new PrintStream(clientSocket.getOutputStream());
			output.println("Hello Server");
			
			input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			String message = input.readLine();
			System.out.println(message);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
