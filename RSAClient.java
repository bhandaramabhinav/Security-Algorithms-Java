import java.io.*;
import java.net.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import javax.crypto.Cipher;

import java.io.*;
public class RSAClient {
	
	public static void main(String[] args) throws Exception {
		try{
		String host = "localhost";
		int port = 7770;
		Socket s = new Socket(host, port);
		ObjectOutputStream ClientTransfer = new ObjectOutputStream(s.getOutputStream());
		ObjectInputStream ServerTransfer = new ObjectInputStream(s.getInputStream());
		KeyPairGenerator genKey = KeyPairGenerator.getInstance("RSA");
		SecureRandom random=new SecureRandom();
		int length=1024;
		genKey.initialize(length, random);
		KeyPair pair=genKey.generateKeyPair();
		RSAPublicKey clientPublicKey=(RSAPublicKey)pair.getPublic();
		RSAPrivateKey clientPrivateKey=(RSAPrivateKey)pair.getPrivate();
		//Get servers public key
		RSAPublicKey serverPublicKey=(RSAPublicKey)ServerTransfer.readObject();
		//Send client public key to server
		ClientTransfer.writeObject(clientPublicKey);
		Cipher RSACipher=Cipher.getInstance("RSA");
		System.out.println("Please enter the text:");
		Scanner input=new Scanner(System.in);
		String plainText=input.nextLine();
		byte[] inputBytes=plainText.getBytes("UTF8");
		System.out.println("1. Confidentiality: In this method we use the servers public key to encipher the Plain Text and send it accross to the sever");
		RSACipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);		
		byte[] cipherBytes=RSACipher.doFinal(inputBytes);
		String cipheredText=Base64.getEncoder().encodeToString(cipherBytes);
		System.out.println("Cipher Text: "+cipheredText);
		ClientTransfer.writeChar('c');
		ClientTransfer.writeObject(cipherBytes);		
		System.out.println("2. Integrity and Authentication: In this method we use the Clients private key to encipher the Plain Text and send it accross to the sever");
		RSACipher.init(Cipher.ENCRYPT_MODE,clientPrivateKey);
		byte[] cipherBytesInteg=RSACipher.doFinal(inputBytes);
		String cipheredTextInteg=Base64.getEncoder().encodeToString(cipherBytesInteg);
		System.out.println("Ciphered Text: "+cipheredTextInteg);		
		ClientTransfer.writeChar('i');
		ClientTransfer.writeObject(cipherBytesInteg);		
		System.out.println("3. Confidentiality & Integrity/Authentication: In this method we use the Clients private key to encipher the Plain Text and send it accross to the sever");
		RSACipher.init(Cipher.ENCRYPT_MODE,clientPrivateKey);
		byte[] cipherBytesIntegC=RSACipher.doFinal(inputBytes);
		String cipheredTextIntegC=Base64.getEncoder().encodeToString(cipherBytesInteg);
		System.out.println("Ciphered Text After Integrity is: "+cipheredTextIntegC);
		RSACipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);
		byte[] cipherBytesConfidC=RSACipher.doFinal(cipherBytesIntegC);		
		String cipheredTextConfidC=Base64.getEncoder().encodeToString(cipherBytesConfidC);
		System.out.println("Ciphered Text After Confidentiality is: "+cipheredTextConfidC);		
		ClientTransfer.writeChar('b');
		ClientTransfer.writeObject(cipherBytesConfidC);		
		}catch(Exception ex){
			System.out.println("Client Error: "+ex.getMessage());
		}
	}

}
