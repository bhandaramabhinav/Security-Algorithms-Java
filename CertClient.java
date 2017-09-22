import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.sql.Date;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.*;

public class CertClient {
	public static void main(String[] args) throws Exception {

		String host = "localhost";
		String filename = "server.cer";
		String message = "The quick brown fox jumps over the lazy dog.";
		int port = 12345;
		Socket s = new Socket(host, port);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
		InputStream inputStream = new FileInputStream(filename);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		X509Certificate cert = (X509Certificate) cf.generateCertificate(inputStream);
		inputStream.close();
		// Read the certificate file
		System.out.println("Certificate Content");
		// Print the contents of the certificate
		System.out.println(cert.toString());		
		java.util.Date date = cert.getNotAfter();
		if (date.after(new java.util.Date())) {
			try {
				cert.checkValidity();// Check server's signature
				RSAPublicKey Server_PublicKey = (RSAPublicKey) cert.getPublicKey();
				// Retrieve the public key from the certificate
				Cipher objCipher = Cipher.getInstance("RSA");
				objCipher.init(Cipher.ENCRYPT_MODE, Server_PublicKey);
				byte[] cipherText = objCipher.doFinal(message.getBytes());
				System.out.println("CipherText: " + cipherText);
				os.writeObject(cipherText);
				os.flush();
				os.close();
				s.close();
				inputStream.close();
			} catch (Exception e) {
				System.out.println(e);
			}

		} else {
			System.out.println("The certificate has expired.");
		}

	}

}