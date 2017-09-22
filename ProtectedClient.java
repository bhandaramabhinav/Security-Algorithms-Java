import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.io.*;

public class ProtectedClient {
	public void sendAuthentication(String user, String password, OutputStream outStream)
			throws IOException, NoSuchAlgorithmException {
		DataOutputStream out = new DataOutputStream(outStream);
		Random ran = new Random();
		// Initializing time stamps and random numbers to generate the Digests
		// which are sent to the server for
		// Double strength password Authentication.
		double randomnumber1 = ran.nextDouble();
		double randomnumber2 = ran.nextDouble();

		Date objDate = new Date();
		Long timestmp1 = objDate.getTime();
		Long timestmp2 = objDate.getTime();
		byte[] userNameBytes = user.getBytes();

		// Calling the protectiob digest methods to get the double strength
		// message digests
		byte[] digest1 = Protection.makeDigest(userNameBytes, timestmp1, randomnumber1);
		byte[] digest2 = Protection.makeDigest(user, password, timestmp2, randomnumber2);
		// Sending user inputs to the server.
		out.writeUTF(user);
		out.writeLong(timestmp1);
		out.writeLong(timestmp2);
		out.writeDouble(randomnumber1);
		out.writeDouble(randomnumber2);
		out.writeInt(digest1.length);
		out.writeInt(digest2.length);
		out.write(digest1);
		out.write(digest2);
		out.flush();
	}

	public static void main(String[] args) throws Exception {
		String host = "localhost";
		int port = 7770;
		String Username="",password="";
		Scanner scan=new Scanner(System.in);
		System.out.println("Enter User name:");
		Username=scan.nextLine();
		System.out.println("Enter Password:");
		password=scan.nextLine();
		//String user = "George";
		//String password = "abc123";
		Socket s = new Socket(host, port);

		ProtectedClient client = new ProtectedClient();
		client.sendAuthentication(Username, password, s.getOutputStream());

		s.close();
	}
}