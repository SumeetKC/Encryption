package com.uhg.encrypt;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Encryptor {

	public static void main(String[] args) {
		String key = "Bar12345Bar12345"; // 128 bit key
		String initVector = "RandomInitVector"; // 16 bytes IV
		String input = null;

		String prptyFileName = "C:\\Users\\605517\\Desktop\\Encrypt&Decrypt\\EDB-dev-DBConfig.properties";
		Properties prop = new Properties();
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(prptyFileName);
			prop.load(inputStream);
			input = prop.getProperty("PASSWORD");
			System.out.println("Input Value is " + input);
			String encrypted_text = encrypt(key, initVector, input);
			System.out.println("encrypted_text is "+ encrypted_text);
			String decrypted_text = decrypt(key, initVector, encrypted_text);
			System.out.println("decrypted_text is "+ decrypted_text);
			inputStream.close();

			outputStream = new FileOutputStream(prptyFileName);
			prop.setProperty("PASSWORD", encrypted_text);
			prop.store(outputStream, null);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static String encrypt(String key, String initVector, String value) {
		try {
			IvParameterSpec iv = new IvParameterSpec(
					initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"),
					"AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes());
			System.out.println("Encrypted String : "
					+ Base64.encodeBase64String(encrypted));

			return Base64.encodeBase64String(encrypted);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public static String decrypt(String key, String initVector, String encrypted) {
		try {
			IvParameterSpec iv = new IvParameterSpec(
					initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"),
					"AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

			byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
			System.out.println("Decrypted String : " + new String(original));
			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	
	/** This is another encryption/decryption method which performs encryption by shifting ASCII
	 Values of the characters by 1 **/
/*	private static String encode(char[] pwd) {
		for (int index = 0; index < pwd.length; index++) {
			pwd[index]++;
		}
		return new String(pwd);
	}
	
	  public static String decode(char[] pwd) {
		  
		 for (int index = 0; index < pwd.length; index++) { pwd[index]--; } String
		 pass=new String(pwd); return pass; }
*/
}