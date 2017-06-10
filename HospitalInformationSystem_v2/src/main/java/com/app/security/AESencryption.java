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
	

	public AESencryption() {
		Security.addProvider(new BouncyCastleProvider());
	}

	// public void testIt()
	// throws InvalidKeyException, NoSuchAlgorithmException,
	// NoSuchProviderException, NoSuchPaddingException {
	// SecretKey secretKey;
	// String data = "Ovo su podaci koji se kriptuju simetricnim AES algoritmom,
	// duzina podataka nije bitna, tj. AES moze da se koristi za proizvoljnu
	// duzinu podataka";
	//
	// System.out.println("===== Primer simetricne AES sifre =====");
	// System.out.println("Podaci koji se sifruju: " + data);
	//
	// System.out.println("\n===== Generisanje kljuca =====");
	// secretKey = generateKey();
	// System.out.println("Generisan kljuc: " +
	// Base64Utility.encode(secretKey.getEncoded()));
	//
	// System.out.println("\n===== Sifrovanje =====");
	// byte[] cipherText = encrypt(data, secretKey);
	// System.out.println("Sifrat: " + Base64Utility.encode(cipherText));
	//
	// System.out.println("\n===== Desifrovanje =====");
	// byte[] plainText = decrypt(cipherText, secretKey);
	// System.out.println("Originalna poruka: " + new String(plainText));
	// }

	public byte[] encrypt(String plainText) {
		int index = System.getProperty("user.dir").indexOf("HospitalSystem");
		String keyStoreFile = System.getProperty("user.dir").substring(0, index);
		keyStoreFile = keyStoreFile + "HospitalSystem\\KMJ2.keystore";
		
		SecretKey key = keyStoreReader.readSecretKey(keyStoreFile, "kmjkmj", "kmj128", "kmjkmj");
		try {
			Cipher desCipherEnc = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
			desCipherEnc.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));

			// sifrovanje
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
		int index = System.getProperty("user.dir").indexOf("HospitalSystem");
		String keyStoreFile = System.getProperty("user.dir").substring(0, index);
		keyStoreFile = keyStoreFile + "HospitalSystem\\KMJ2.keystore";
		
		SecretKey key = keyStoreReader.readSecretKey(keyStoreFile, "kmjkmj", "kmj128", "kmjkmj");
		try {
			Cipher desCipherDec = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
			desCipherDec.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));

			// desifrovanje
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
