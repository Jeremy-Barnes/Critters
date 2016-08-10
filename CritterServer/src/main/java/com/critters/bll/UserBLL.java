package com.critters.bll;

import com.critters.dal.dto.User;
import com.lambdaworks.codec.Base64;
import com.lambdaworks.crypto.SCrypt;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * Created by Jeremy on 8/9/2016.
 */
public class UserBLL {


	/***************** SECURITY STUFF **********************/
	private static void hashAndSaltPassword(User user) throws GeneralSecurityException, UnsupportedEncodingException {
		byte[] saltByte = new byte[16];
		SecureRandom.getInstance("SHA1PRNG").nextBytes(saltByte);
		String saltStr = new String(Base64.encode(saltByte));

		byte[] hashByte = SCrypt.scrypt(user.getPassword().getBytes("UTF-8"), saltStr.getBytes("UTF-8"), 16384, 8, 1, 64);
		String hashStr = new String(Base64.encode(hashByte));

		user.setPassword(hashStr);
		user.setSalt(saltStr);
	}

	private static String createSelectorAndHashValidator(User user) throws GeneralSecurityException, UnsupportedEncodingException {
		byte[] validatorByte = new byte[16];
		SecureRandom.getInstance("SHA1PRNG").nextBytes(validatorByte);
		String validatorStr = new String(Base64.encode(validatorByte)); //Get in UTF
		byte[] hashedValidatorByte = SCrypt.scrypt(validatorStr.getBytes("UTF-8"), validatorStr.getBytes("UTF-8"), 16384, 8, 1, 64);

		user.setTokenSelector(UUID.randomUUID().toString());
		user.setTokenValidator(new String(Base64.encode(hashedValidatorByte)));
		String uuid = UUID.randomUUID().toString();
		return validatorStr;
	}

	protected static boolean verifyValidator(String suppliedValidator, User dbUser) throws GeneralSecurityException, UnsupportedEncodingException {
		byte[] hashByte = SCrypt.scrypt(suppliedValidator.getBytes("UTF-8"), suppliedValidator.getBytes("UTF-8"), 16384, 8, 1, 64);
		String hashedCookieValidator = new String(Base64.encode(hashByte));
		return hashedCookieValidator.equals(dbUser.getTokenValidator());
	}

	private static boolean checkLogin(String dbPasswordHash, String suppliedPassword, String suppliedSalt) throws GeneralSecurityException, UnsupportedEncodingException {
		byte[] hashByte = SCrypt.scrypt(suppliedPassword.getBytes("UTF-8"), suppliedSalt.getBytes("UTF-8"), 16384, 8, 1, 64);
		String hashStrConfirm = new String(Base64.encode(hashByte));
		return hashStrConfirm.equals(dbPasswordHash);
	}
}
