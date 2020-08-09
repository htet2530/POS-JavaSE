package com.jdc.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PosEncrypter {
	public static String encrypt(String password) {
		try {
			MessageDigest message = MessageDigest.getInstance("SHA-256");
			byte[] encode = message.digest(password.getBytes());
			return Base64.getEncoder().encodeToString(encode);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
