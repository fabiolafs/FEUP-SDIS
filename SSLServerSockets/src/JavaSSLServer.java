 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocketFactory;
 
/**
 * @web http://java-buddy.blogspot.com/
 */
public class JavaSSLServer {
     
    static final int port = 8000;
    //static ArrayList<JavaSSLServerListener> acceptSocket = new ArrayList<JavaSSLServerListener>();

    public static void main(String[] args) {
        
    		Socket socket;
    		
		System.setProperty("javax.net.ssl.keyStore","mykeystore");
		System.setProperty("javax.net.ssl.keyStorePassword","1234567890");
		
        SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
         
        try {
            ServerSocket sslServerSocket = sslServerSocketFactory.createServerSocket(port);
            System.out.println("SSL ServerSocket started");
            System.out.println(sslServerSocket.toString());
             
            while(true) {
	            socket = sslServerSocket.accept();
	            System.out.println("ServerSocket accepted");
	            JavaSSLServerListener channel = new JavaSSLServerListener(socket);
	            new Thread(channel).start();
	            //acceptSocket.add(channel);
            }
             
        } catch (IOException ex) {
            Logger.getLogger(JavaSSLServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
}