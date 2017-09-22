import java.io.*;
import java.net.*;
import java.security.*;

import javax.crypto.*;

public class CertServer {

	public static void main(String[] args) throws Exception 
	{   
		int port = 12345;
		ServerSocket servername = new ServerSocket(port);
		Socket s = servername.accept();
		String name="selfsigned";
        char[] serverpassword="password".toCharArray();        
		ObjectInputStream is = new ObjectInputStream(s.getInputStream());
		//Read the keystore and retrieve the server's private key		
        KeyStore ks = KeyStore.getInstance("jks");
        ks.load(new FileInputStream("keystore.jks"), serverpassword);
        PrivateKey Server_PrivateKey = (PrivateKey)ks.getKey(name, serverpassword);       
        //Decrypt: server's private key 
        Cipher cipher = Cipher.getInstance("RSA");
        byte[] in = (byte[]) is.readObject();
		cipher.init(Cipher.DECRYPT_MODE, Server_PrivateKey);
		byte[] plaintText = cipher.doFinal(in);
		System.out.println("Message Received: " + new String(plaintText));
		servername.close();
	}
}

