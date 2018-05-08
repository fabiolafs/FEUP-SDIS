import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class JavaSSLServerListener implements Runnable{

	Socket socket;
	public JavaSSLServerListener(Socket s){
		socket = s;
	}
	
	public void run() {
		System.out.println("Listener started");
		PrintWriter out = null;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	        
	        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
	           
	        	String line = null;

            while((line = bufferedReader.readLine()) != null){
                System.out.println(line);
                out.println(line);
            }
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println("Closed");
	}

}
