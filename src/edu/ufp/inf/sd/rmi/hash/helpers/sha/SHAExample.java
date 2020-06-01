package edu.ufp.inf.sd.rmi.hash.helpers.sha;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class SHAExample {
	
	public static String get_SHA_512_SecurePassword(String passwordToHash) throws NoSuchAlgorithmException {
		String salt = getSalt();
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt.getBytes());
			byte[] bytes = md.digest(passwordToHash.getBytes());
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< bytes.length ;i++)
			{
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		}
		return generatedPassword;
	}

	//Add salt
	public static String getSalt() throws NoSuchAlgorithmException
	{
		/*
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			byte[] salt = new byte[16];
			sr.nextBytes(salt);
			return salt.toString();
		*/
		return "[B@2096442d";
	}
}
