import java.io.*;
import java.security.*;

public class Protection
{
	public static byte[] makeBytes(long t, double q) 
	{    
		try 
		{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			DataOutputStream dataOut = new DataOutputStream(byteOut);
			dataOut.writeLong(t);
			dataOut.writeDouble(q);
			return byteOut.toByteArray();
		}
		catch (IOException e) 
		{
			return new byte[0];
		}
	}	

	public static byte[] makeDigest(byte[] mush, long t2, double q2) throws NoSuchAlgorithmException 
	{
		MessageDigest md = MessageDigest.getInstance("SHA");
		md.update(mush);
		md.update(makeBytes(t2, q2));
		return md.digest();
	}
	

	public static byte[] makeDigest(String user, String password,
									long t1, double q1)
									throws NoSuchAlgorithmException
	{
		MessageDigest objMessageDigest=MessageDigest.getInstance("SHA");
		byte[] userNameBytes=user.getBytes();
		byte[] passwordInBytes=password.getBytes();
		byte[] timestmprannodigest=Protection.makeBytes(t1, q1);
		objMessageDigest.update(userNameBytes);
		objMessageDigest.update(passwordInBytes);
		objMessageDigest.update(timestmprannodigest);
		byte[] result=objMessageDigest.digest();
		return result;
	}
}