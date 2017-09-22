import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Dictionary;
import java.util.HashMap;

public class ProtectedServer {
	public HashMap<String, String> userdetails = new HashMap<String, String>();

	ProtectedServer() {
		initializeUserDetails();
	}

	public boolean authenticate(InputStream inStream) throws IOException, NoSuchAlgorithmException {
		DataInputStream in = new DataInputStream(inStream);
		String username = in.readUTF();
		long timestmp1 = in.readLong();
		long timestmp2 = in.readLong();
		double randomnumber1 = in.readDouble();
		double randomnumber2 = in.readDouble();
		int length1 = in.readInt();
		int length2 = in.readInt();
		String userpassword = lookupPassword(username);
		byte[] calculatedFinalDigest = new byte[20];
		byte[] calculatedInitialDigest = new byte[20];
		in.readFully(calculatedInitialDigest);
		in.readFully(calculatedFinalDigest);
		byte[] serverCalculatedInitialDigest = Protection.makeDigest(username.getBytes(), timestmp1, randomnumber1);
		byte[] serverCalculatedFinalDigest = Protection.makeDigest(username, userpassword, timestmp2, randomnumber2);
		if (MessageDigest.isEqual(calculatedFinalDigest, serverCalculatedFinalDigest)
				&& MessageDigest.isEqual(calculatedInitialDigest, serverCalculatedInitialDigest)) {
			return true;
		} else {
			return false;
		}
	}

	private void initializeUserDetails() {
		userdetails.put("George", "abc123");
		userdetails.put("Anuj", "password");
		userdetails.put("Hassan Takabi", "pass1234");
		userdetails.put("Abhinav", "test123");
		userdetails.put("Srikanth", "Test123");
	}

	protected String lookupPassword(String user) {
		return userdetails.get(user);
	}

	public static void main(String[] args) throws Exception {
		ServerSocket s = null;
		try {
			int port = 7770;
			s = new ServerSocket(port);
			Socket client = s.accept();

			ProtectedServer server = new ProtectedServer();

			if (server.authenticate(client.getInputStream()))
				System.out.println("Client logged in.");
			else
				System.out.println("Client failed to log in.");

			s.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			if (s != null && !s.isClosed()) {
				s.close();
			}
		}
	}
}