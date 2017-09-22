import java.security.*;
import java.util.Scanner;
import java.io.*;
import org.apache.commons.codec.binary.Hex;

public class MessageDigestAlgorithm {
	public static void main(String[] args) {
		try {
			Scanner scan = new Scanner(System.in);
			String inputString = "", hashingAlgorithm = "";
			int selection = 0;
			do {
				System.out.println("");
				System.out.println("1. SHA Algorithm");
				System.out.println("2. MD5 Algorithm");
				System.out.println("3. Exit");
				System.out.println("Please select the hashing algorithm:");
				selection = Integer.parseInt(scan.nextLine());				
				//System.out.println("sel:" + selection);
				switch (selection) {
				case 1:
					hashingAlgorithm = "SHA";
					break;
				case 2:
					hashingAlgorithm = "MD5";
					break;
				case 3:
					System.exit(1);
					break;

				default:
					System.out.println("Please enter valid input");
					hashingAlgorithm="";
					inputString="";
					break;
				}
				if(hashingAlgorithm!=""){
				System.out.println("Enter Input String:");
				if (scan.hasNext())
					inputString = scan.nextLine();
				MessageDigestAlgorithm.CalculateHash(hashingAlgorithm, inputString);
				}
				
			} while (selection != 0);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			String error = e.getMessage();
			System.out.println(error);
		}

	}

	public static void CalculateHash(String algorithm, String inputString) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance(algorithm);
			byte[] input = inputString.getBytes();
			md.update(input);
			byte[] outputHashBytes = md.digest();
			String outPutHex = Hex.encodeHexString(outputHashBytes);
			System.out.println(algorithm + " Hash String:" + outputHashBytes.toString());
			System.out.println(algorithm + " Hash Hex:" + outPutHex);
			md.reset();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			String error = e.getMessage();
			System.out.println(error);
		}

	}
}
