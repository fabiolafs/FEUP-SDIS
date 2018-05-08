import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.regex.Pattern;

public class Client {

	private static final Pattern PATTERN = Pattern.compile("([0-9]{2}|[A-Z]{2}|[a-z]{2})-([0-9]{2}|[A-Z]{2}|[a-z]{2})-([0-9]{2}|[A-Z]{2}|[a-z]{2})");
	private static DatagramSocket clientSocket;
	private static InetAddress IPAddress;
	private static DatagramPacket receivePacket;
	private static DatagramPacket requestPacket;
	private static byte[] sendDatas = new byte[1024];
	private static byte[] receiveData = new byte[1024];
	private static int srvc_port;
	private static String srvc_addr;
	private static String request = "";
	private static String sentence;
	//MULTICAST
	private static String mcast_addr;
	private static int mcast_port;
	private static InetAddress groupIPAddress; 
	private static MulticastSocket multicastClientSocket; 
	
	
	
	public static boolean verifyPlate(String plate) {
      if ( PATTERN.matcher(plate).matches() ){
    	  	return true;
      }
      return false;
	}
	
	public static void main(String[] args)  throws Exception {
		
boolean flagPlate;
		
		//java client <mcast_addr> <mcast_port> <oper> <opnd>
		if (args.length < 4) {
			 System.out.println("Necessary arguments ");
	         System.out.println("<mcast_addr> <mcast_port> REGISTER <plate number> <owner name>");
	         System.out.println("<mcast_addr> <mcast_port> LOOKUP <plate number>");
		}else {
			mcast_addr = args[0];
			mcast_port = Integer.parseInt(args[1]);
			
			flagPlate = verifyPlate(args[3]);
			
			if (args.length == 5 && args[2].equals("REGISTER") && flagPlate) {
				request = "REGISTER " + args[3] + " " + args[4];
    				
			}else if (args.length == 4  && args[2].equals("LOOKUP") && flagPlate) {
				request = "LOOKUP " + args[3];
	        }
			
			System.out.println(request + " <-> " + (flagPlate ? "Valid plate!" : "ERRO! Invalid plate") );
		}
		
		
		//JOIN MULTICAST GROUP
		groupIPAddress = InetAddress.getByName(mcast_addr);
		multicastClientSocket = new MulticastSocket(mcast_port);
		multicastClientSocket.joinGroup(groupIPAddress);
		System.out.println("1");
		//RECEIVE UDP INFO FROM SERVER
		receiveData = request.getBytes();
		System.out.println("2");
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		System.out.println("3");
	    multicastClientSocket.receive(receivePacket);
	    System.out.println("4");
	    sentence = new String(receivePacket.getData(),0,receivePacket.getLength());
		String[] receivedMsg = sentence.split(" ");
		srvc_addr = receivedMsg[0];
		srvc_port = Integer.parseInt(receivedMsg[1]);
		System.out.println("5");
		
		//most print
		//multicast: <mcast_addr> <mcast_port>: <srvc_addr> <srvc_port>  <oper> <opnd> *:: <out>
		System.out.println("multicast: " + mcast_addr + ", " + mcast_port + ", " + srvc_addr + ", " + srvc_port + ". " + request);
		
	    //END
		multicastClientSocket.leaveGroup(groupIPAddress);
		multicastClientSocket.close();

		//START CONECTION
		IPAddress = InetAddress.getByName(srvc_addr);		
		clientSocket = new DatagramSocket();
		
		//SEND REQUEST
		sendDatas = request.getBytes();
	    requestPacket = new DatagramPacket(sendDatas, sendDatas.length, IPAddress, srvc_port);
		clientSocket.send(requestPacket);
		
		//RECEIVE RESPONSE
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		sentence = new String(receivePacket.getData(),0,receivePacket.getLength());
		System.out.println(sentence);
	}

}








