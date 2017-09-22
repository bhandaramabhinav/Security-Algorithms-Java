import java.io.*;
import java.net.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;

import java.io.*;

public class RSAServer {

	public static void main(String[] args) throws Exception {
		try{
		int port = 7770;
		ServerSocket server = new ServerSocket(port);
		Socket s = server.accept();
		ObjectInputStream ClientTransfer = new ObjectInputStream(s.getInputStream());
		ObjectOutputStream ServerTransfer = new ObjectOutputStream(s.getOutputStream());
		SecureRandom random=new SecureRandom();
		int length=1024;
		/* Generate Bob's key pairs (private key and public key)*/
		KeyPairGenerator genKey = KeyPairGenerator.getInstance("RSA");
	    genKey.initialize(length, random); 
	    KeyPair Key = genKey.genKeyPair();
	    RSAPublicKey Server_PublicKey = (RSAPublicKey) Key.getPublic();
	    RSAPrivateKey Server_PrivateKey= (RSAPrivateKey)Key.getPrivate();
	    ServerTransfer.writeObject(Server_PublicKey);
	    RSAPublicKey Client_PublicKey=(RSAPublicKey)ClientTransfer.readObject();
	    Cipher cipher=Cipher.getInstance("RSA");
	    System.out.println("1. Confidentiality:");
	    char chC=ClientTransfer.readChar();
	    byte[] encipheredTextC=(byte[])ClientTransfer.readObject();
	    cipher.init(Cipher.DECRYPT_MODE, Server_PrivateKey);
	    byte[] decipheredBytesC=cipher.doFinal(encipheredTextC);
	    String DecipeheredTextC=new String(decipheredBytesC,"UTF8");
	    System.out.println("Deciphered Text: "+DecipeheredTextC);
	    System.out.println("2. Integrity/Authentication:");
	    char chI=ClientTransfer.readChar();
	    byte[] encipheredTextI=(byte[])ClientTransfer.readObject();
	    cipher.init(Cipher.DECRYPT_MODE, Client_PublicKey);
	    byte[] decipheredBytesI=cipher.doFinal(encipheredTextI);
	    String DecipeheredTextI=new String(decipheredBytesI,"UTF8");
	    System.out.println("Deciphered Text: "+DecipeheredTextI);
	    System.out.println("3. Both:");
	    char chB=ClientTransfer.readChar();
	    byte[] encipheredTextIB=(byte[])ClientTransfer.readObject();
	    cipher.init(Cipher.DECRYPT_MODE, Server_PrivateKey);
	    byte[] decipheredBytesIB=cipher.doFinal(encipheredTextI);
	    cipher.init(Cipher.DECRYPT_MODE, Client_PublicKey);
	    byte[] decipheredBytesIB2=cipher.doFinal(decipheredBytesIB);
	    String DecipeheredTextIB=new String(decipheredBytesIB2,"UTF8");
	    System.out.println("Deciphered Text: "+DecipeheredTextIB);
		}catch(Exception ex){
			System.out.println("Server Error: "+ ex.getMessage());
		}
		
		
	}

}
