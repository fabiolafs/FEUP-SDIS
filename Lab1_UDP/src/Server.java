import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class Server {

	private static DatagramSocket serverSocket;
	private static DatagramPacket receivePacket;
	private static byte[] sendDatas  = new byte[1024];
	private static byte[] receiveData  = new byte[1024];
	private static int port_number;
	private static Map<String,String> plates = new HashMap<String,String>();
	private static String response = "";
	
	private static String registerPlate(String plateNumber, String ownerName) {
		plates.put(plateNumber, ownerName);
		response = "ADDED PLATE SUCCESSFULLY! <-> number: "+ plateNumber + ", owner: " + ownerName;
		return response;
	}
	
	private static String lookupPlate(String plateNumber) {
		String ownerName = plates.get(plateNumber);
		response = "NOT FOUND! <-> Plane number: " + plateNumber;
		
		if( ownerName != null ) {
			response = "FOUND! <-> Plane number: "+ plateNumber + ", Owner: " + ownerName;
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
	
	public static void main(String args[]) throws Exception {

		if(args.length != 0) {
			port_number = Integer.parseInt(args[0]);
		}
		
		//START CONECTION
		serverSocket = new DatagramSocket(port_number);
		
		while (true) {
			
			//RECEIVE REQUSNTE
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			String sentence = new String(receivePacket.getData(),0,receivePacket.getLength());
			String[] receivedMsg = sentence.split(" ");
			
			//PROCESS REQUEST
			response = processSentence(receivedMsg);
			System.out.println(response);
			
			//SEND RESPONSE
			sendDatas = response.getBytes();
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			DatagramPacket sendPacket = new DatagramPacket(sendDatas, sendDatas.length, IPAddress, port);
			serverSocket.send(sendPacket);
		}
	}

}
