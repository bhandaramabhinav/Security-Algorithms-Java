import java.io.*;
import java.net.*;
import java.security.*;
import java.util.concurrent.ExecutionException;
import java.math.BigInteger;

public class ElGamalAlice {
	private static BigInteger computeY(BigInteger p, BigInteger g, BigInteger d) {
		// Using inbuilt function modPow which when used with a big integer
		// variable z with
		// input parameters as 2 bing integer variables x and y gives the output
		// calculated as
		// z^x mod y
		// for our program y=g^d mod p
		BigInteger y = g.modPow(d, p);
		return y;
	}

	private static BigInteger computeK(BigInteger p) {
		// Creating a secure random number object to use to create a BigInteger
		// object.
		SecureRandom random = new SecureRandom();
		// Setting the bit length size of the BigInteger number to 1024 same as
		// in main method.
		int bitStrength = 1024;
		// Setting the value of p to p-l as Alice selects K such that K is
		// relatively prime to p-1.
		p = p.subtract(BigInteger.ONE);
		// Generating the random Biginteger value and assigning it to a temp
		// variable.
		BigInteger temp = new BigInteger(bitStrength, random);
		// Calculating the gcd og randomly generated temp value and p-1 and
		// assigning it to gcd.
		BigInteger gcd = temp.gcd(p);
		// checking the value of gcd if it one or not, because the gcd of 2
		// relatively prime numbers is 1
		// if the gcd is one the current randomly generated value of temp is
		// relatively prime to p-1.
		while (!gcd.equals(BigInteger.ONE)) {
			// generating a new value for temp as the old temp variable is not
			// relatively prime
			// p.
			temp = new BigInteger(bitStrength, random);
			// calculating the gcd value of the new temp variable and p.
			gcd = temp.gcd(p);
		}
		// assigning the value of temp to k.
		// BigInteger k = temp;
		// returning k.
		BigInteger k = new BigInteger(String.valueOf(5));
		return k;
	}

	private static BigInteger computeA(BigInteger p, BigInteger g, BigInteger k) {
		// calculating a=3^5 mod 29
		// a=g^k mod p
		return g.modPow(k, p);
	}

	private static BigInteger computeB(String message, BigInteger d, BigInteger a, BigInteger k, BigInteger p) {
		// we calculate b such that m=(da+kb)mod p-1
		// therefore b can be calculated using the formula
		// b=(m-da)k^-1 mod(p-1)
		// formula reference:
		// http://www.cse.unt.edu/~mgomathi/teaching/2009/csce5550/Lectures/Lecture%2013.pdf
		// get the biginteger value of m
		try {
			BigInteger M = new BigInteger(message.getBytes());
			//BigInteger M=new BigInteger(String.valueOf(23));
			//System.out.println(M);
			// calculate da
			BigInteger DA = d.multiply(a);
			System.out.println(DA);
			// calculate m-da
			BigInteger mMinDA = M.subtract(DA);
			//System.out.println(mMinDA);
			// calculate k^-1modp-1
			BigInteger K = BigInteger.ONE.divide(k);
			//System.out.println(K);
			// calculate K^-1mod(p-1)
			BigInteger KmodPmin1=k.modInverse(p.subtract(BigInteger.ONE));
			//System.out.println(KmodPmin1);
			//multply (m-da)*k^-1modp-1
			BigInteger B=mMinDA.multiply(KmodPmin1);			
			return B;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static void main(String[] args) throws Exception {
		String message = "The quick brown fox jumps over the lazy dog.";

		String host = "localhost";
		int port = 1234;
		Socket s = new Socket(host, port);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

		// You should consult BigInteger class in Java API documentation to find
		// out what it is.
		BigInteger y, g, p; // public key
		BigInteger d; // private key

		int mStrength = 1024; // key bit length
		SecureRandom mSecureRandom = new SecureRandom(); // a cryptographically
															// strong
															// pseudo-random
															// number

		// Create a BigInterger with mStrength bit length that is highly likely
		// to be prime.
		// (The '16' determines the probability that p is prime. Refer to
		// BigInteger documentation.)
		p = new BigInteger(mStrength, 16, mSecureRandom);

		// Create a randomly generated BigInteger of length mStrength-1
		g = new BigInteger(mStrength - 1, mSecureRandom);
		d = new BigInteger(mStrength - 1, mSecureRandom);

		y = computeY(p, g, d);

		// At this point, you have both the public key and the private key. Now
		// compute the signature.

		BigInteger k = computeK(p);
		BigInteger a = computeA(p, g, k);
		BigInteger b = computeB(message, d, a, k, p);

		// send public key
		os.writeObject(y);
		os.writeObject(g);
		os.writeObject(p);

		// send message
		os.writeObject(message);

		// send signature
		os.writeObject(a);
		os.writeObject(b);

		// s.close();
	}
}