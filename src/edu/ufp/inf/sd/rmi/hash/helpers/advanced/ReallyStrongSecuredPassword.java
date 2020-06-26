package edu.ufp.inf.sd.rmi.hash.helpers.advanced;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public final class ReallyStrongSecuredPassword
{
	public static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		int iterations = 1000;
		byte[] salt = getSalt().getBytes();

		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, 128);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hash = factory.generateSecret(spec).getEncoded();
		 return toHex(hash);
	}
	
	public static String getSalt() throws NoSuchAlgorithmException
	{
		//SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		//byte[] salt = new byte[16];
		//sr.nextBytes(salt);
		return "[B@2096442d";
	}

	private static String toHex(byte[] array) throws NoSuchAlgorithmException
	{
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if(paddingLength > 0)
		{
			return String.format("%0"  +paddingLength + "d", 0) + hex;
		}else{
			return hex;
		}
	}

}
