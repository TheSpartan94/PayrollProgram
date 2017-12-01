package it.unipv.payroll.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Hash {
	
	// this function is used to obtain the MD5 hash for a  given string
	static public String getmd5(String s) throws NoSuchAlgorithmException { 
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(s.getBytes());
		byte[] digest = m.digest();
		BigInteger bigInt = new BigInteger(1, digest);
		return bigInt.toString(16);
	}

}
