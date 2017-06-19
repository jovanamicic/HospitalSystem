package com.app.security;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class AESencryption {
	
	@Autowired
	private KeyStoreReader keyStoreReader;
	
	static String IV = "AAAAAAAAAAAAAAAA";
	
	private final String KEY_STORE_FILE = ".\\keystores\\AES_key.keystore";
	private final String KEY_STORE_PASS = "keykey";
	private final String ALIAS = "aes128";
	private final String PASS = "keykey";
	
	

	public AESencryption() {
		Security.addProvider(new BouncyCastleProvider());
	}

	public byte[] encrypt(String plainText) {
		
		SecretKey key = keyStoreReader.readSecretKey(KEY_STORE_FILE, KEY_STORE_PASS, ALIAS, PASS);
		try {
			Cipher desCipherEnc = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
			desCipherEnc.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));

			byte[] ciphertext = desCipherEnc.doFinal(plainText.getBytes());
			return ciphertext;
			
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public byte[] decrypt(byte[] cipherText)
			throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException {
		
		SecretKey key = keyStoreReader.readSecretKey(KEY_STORE_FILE, KEY_STORE_PASS, ALIAS, PASS);
		
		try {
			Cipher desCipherDec = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
			desCipherDec.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));

			byte[] plainText = desCipherDec.doFinal(cipherText);
			return plainText;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public byte[] longToBytes(long x) {
	    ByteBuffer buffer = ByteBuffer.allocate(32);
	    buffer.putLong(x);
	    return buffer.array();
	}

	public long bytesToLong(byte[] bytes) {
	    ByteBuffer buffer = ByteBuffer.allocate(32);
	    buffer.put(bytes);
	    buffer.flip();
	    return buffer.getLong();
	}
}
