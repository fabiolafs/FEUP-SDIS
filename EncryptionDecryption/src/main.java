
public class main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		String fileName;
		String encryptionPassword;
		
		
		if ( args.length == 2) {
			encryptionPassword = args[0];
			fileName = args[1];
		}
		else
			return;
		
		//Encryption
		AESFileEncryption AESFileEncryption = new AESFileEncryption();
		AESFileEncryption.Encryption(encryptionPassword, fileName);
	
		//Decryption
		AESFileDecryption AESFileDecryption = new AESFileDecryption();
		AESFileDecryption.Decryption(encryptionPassword, fileName);
		
	}

}
