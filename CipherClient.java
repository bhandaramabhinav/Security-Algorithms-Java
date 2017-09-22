import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Base64;

import javax.crypto.*;

public class CipherClient {
	public static void main(String[] args) throws Exception {
		String message = "The quick brown fox jumps over the lazy dog.";
		String host = "localhost";
		String filename = "Key.txt";
		int port = 8888;
		Socket s = new Socket(host, port);
		SecureRandom random = new SecureRandom();
		BufferedWriter bw = null;
		FileWriter fw = null;
		KeyGenerator keygen = KeyGenerator.getInstance("DES");
		keygen.init(random);
		SecretKey privateKey = keygen.generateKey();
		fw = new FileWriter(filename);
		bw = new BufferedWriter(fw);
		String privateStrinKey=Base64.getEncoder().encodeToString(privateKey.getEncoded());		
		bw.write(privateStrinKey);
		bw.close();
		Cipher objCipher=Cipher.getInstance("DES");		
		objCipher.init(Cipher.ENCRYPT_MODE, privateKey);		
		byte[] messageBytes=message.getBytes("UTF8");
		byte[] cipherBytes=objCipher.doFinal(messageBytes);
		String cipherText=Base64.getEncoder().encodeToString(cipherBytes);
		DataOutputStream out = new DataOutputStream(s.getOutputStream());
		out.writeUTF(cipherText);		
	}
}