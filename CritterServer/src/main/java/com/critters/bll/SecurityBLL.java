package com.critters.bll;

import com.critters.Utilities.Extensions;
import com.lambdaworks.codec.Base64;
import com.lambdaworks.crypto.SCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * Created by Jeremy on 12/30/2017.
 */
public class SecurityBLL {
	private static final SecureRandom sRNG;
	static final Logger logger = LoggerFactory.getLogger("application");

	static {
		try {
			sRNG = SecureRandom.getInstance("SHA1PRNG");
		} catch(NoSuchAlgorithmException ex) {
			logger.error("Couldn't find SHA1PRNG!", ex);
			System.exit(1); //if no secure algorithm is available, the service needs to shut down for emergency maintenance.
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static String getGUID(){
		return UUID.randomUUID().toString();
	}

	public static String getRandomString(int length) {
		byte[] strByte = new byte[length];
		sRNG.nextBytes(strByte);
		return new String(Base64.encode(strByte)); //Get in UTF
	}

	public static String hashAndSaltEncrypt(String password, String salt){
		if(Extensions.isNullOrEmpty(password) ||  Extensions.isNullOrEmpty(salt)) {	return null; }

		String hashword = null;
		try {
			byte[] passwordBytes = password.getBytes("UTF-8");
			byte[] saltBytes = salt.getBytes("UTF-8");
			byte[] hashByte = SCrypt.scrypt(passwordBytes, saltBytes, 16384, 8, 1, 64);
			hashword = new String(Base64.encode(hashByte));
		} catch (UnsupportedEncodingException e) {
			logger.warn("Unsupported encoding on the password: " + password, e);
		} catch (GeneralSecurityException ex) {
			logger.error("Could not run scrypt!", ex);
			System.exit(1); //if no secure algorithm is available, the service needs to shut down for emergency maintenance.
		}
		return hashword;
	}

	public static boolean validateEncryptedMatch(String password, String salt, String encryptedValue) {
		if(Extensions.isNullOrEmpty(password) || Extensions.isNullOrEmpty(encryptedValue) || Extensions.isNullOrEmpty(salt)) {
			return false;
		}
		String hashedValidator = SecurityBLL.hashAndSaltEncrypt(password, salt);
		return Extensions.isNullOrEmpty(hashedValidator) ? false : hashedValidator.equals(encryptedValue);
	}



}
