import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class Client {

	private static final Pattern PATTERN = Pattern.compile("([0-9]{2}|[A-Z]{2}|[a-z]{2})-([0-9]{2}|[A-Z]{2}|[a-z]{2})-([0-9]{2}|[A-Z]{2}|[a-z]{2})");
	private static DatagramSocket clientSocket;
	private static InetAddress IPAddress;
	private static DatagramPacket receivePacket;
	private static DatagramPacket requestPacket;
	private static byte[] sendDatas = new byte[1024];
	private static byte[] receiveData = new byte[1024];
	private static int port_number;
	private static String host_name;
	private static String request = "";
	
	public static boolean verifyPlate(String plate) {
      if ( PATTERN.matcher(plate).matches() ){
    	  	return true;
      }
      return false;
	}
	
	public static void main(String[] args)  throws Exception {
		boolean flagPlate;
		if (args.length < 4) {
            System.out.println("Necessary arguments ");
            System.out.println("<host_name> <port_number> REGISTER <plate number> <owner name>");
            System.out.println("<host_name> <port_number> LOOKUP <plate number>");
            return;
            
		}else {
			port_number = Integer.parseInt(args[0]);
			host_name = args[1];
			flagPlate = verifyPlate(args[3]);
			
			if (args.length == 5 && args[2].equals("REGISTER") && flagPlate) {
				request = "REGISTER " + args[3] + " " + args[4];
    				
			}else if (args.length == 4  && args[2].equals("LOOKUP") && flagPlate) {
				request = "LOOKUP " + args[3];
	        }
			
			System.out.println(request + " <-> " + (flagPlate ? "Valid plate!" : "ERRO! Invalid plate") );
		}
        
		//START CONECTION
		IPAddress = InetAddress.getByName(host_name);		
		clientSocket = new DatagramSocket();
		
		//SEND REQUEST
		sendDatas = request.getBytes();
	    requestPacket = new DatagramPacket(sendDatas, sendDatas.length, IPAddress, port_number);
		clientSocket.send(requestPacket);
		
		//RECEIVE RESPONSE
		String sentence;
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		sentence = new String(receivePacket.getData(),0,receivePacket.getLength());
		System.out.println(sentence);
	}

}
