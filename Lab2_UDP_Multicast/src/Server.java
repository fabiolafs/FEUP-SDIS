import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.Map;

public class Server {

	private static DatagramSocket serverSocket;
	
	private static DatagramPacket receivePacket;
	private static DatagramPacket sendPacket;
	private static byte[] sendDatas  = new byte[1024];
	private static byte[] receiveData  = new byte[1024];
	private static Map<String,String> plates = new HashMap<String,String>();
	private static String response = "";
	private static String sentence;
	
	private static String mcast_addr;
	private static int srvc_port, mcast_port;
	private static InetAddress groupIPAddress; 
	private static MulticastSocket multicastServerSocket; 
	
	
	private static String registerPlate(String plateNumber, String ownerName) {
		plates.put(plateNumber, ownerName);
		response = "ADDED PLATE SUCCESSFULLY! <-> number: "+ plateNumber + ", owner: " + ownerName;
		return response;
	}
	
	private static String lookupPlate(String plateNumber) {
		String ownerName = plates.get(plateNumber);
		response = "NOT FOUND! <-> Plate number: " + plateNumber;
		
		if( ownerName != null ) {
			response = "FOUND! <-> Plate number: "+ plateNumber + ", Owner: " + ownerName;
		}
		
		return response;
	}
	
	private static String processSentence(String[] msg) {
		if (msg.length == 3 && msg[0].equals("REGISTER")) {
			return registerPlate(msg[1], msg[2]);
		}
		else if (msg.length == 2 && msg[0].equals("LOOKUP")) {
			return lookupPlate(msg[1]);
		}
		return "ERRO!";
	}
	
	@SuppressWarnings("resource")
	public static void main(String args[]) throws Exception {

		//java Server <srvc_port> <mcast_addr> <mcast_port> 
		if( args.length == 3) {
			srvc_port = Integer.parseInt(args[0]);
			mcast_addr = args[1];
			mcast_port = Integer.parseInt(args[2]);
		}
		System.out.println( srvc_port + " " + mcast_addr + " " + mcast_port);
		
		
		
		groupIPAddress = InetAddress.getByName(mcast_addr);
		
		DatagramSocket socket = new DatagramSocket();
				
		//SEND UDP PORT AND ADDRESS
		sendDatas = ("localhost " + Integer.toString(srvc_port)  ).getBytes();
		sendPacket = new DatagramPacket(sendDatas, sendDatas.length, groupIPAddress, mcast_port);
		socket.send(sendPacket);
		
		System.out.println("localhost " + srvc_port);
		//most print
		//multicast: <mcast_addr> <mcast_port>: <srvc_addr> <srvc_port> <oper> <opnd> * :: <out>

		
		//START CONECTION
		serverSocket = new DatagramSocket(srvc_port);
		
		while (true) {
			
			//RECEIVE REQUSNTE
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			sentence = new String(receivePacket.getData(),0,receivePacket.getLength());
			String[] receivedMsg = sentence.split(" ");
			
			//PROCESS REQUEST
			response = processSentence(receivedMsg);
			System.out.println(response);
			
			//SEND RESPONSE
			sendDatas = response.getBytes();
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			sendPacket = new DatagramPacket(sendDatas, sendDatas.length, IPAddress, port);
			serverSocket.send(sendPacket);
		}
	}

}

