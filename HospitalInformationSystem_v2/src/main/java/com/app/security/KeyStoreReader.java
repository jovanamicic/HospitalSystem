package com.app.security;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.crypto.SecretKey;

import org.bouncycastle.asn1.x500.X500Name;
import org.springframework.stereotype.Component;


@Component
public class KeyStoreReader {
	//KeyStore je Java klasa za citanje specijalizovanih datoteka koje se koriste za cuvanje kljuceva
	//Tri tipa entiteta koji se obicno nalaze u ovakvim datotekama su:
	// - Sertifikati koji ukljucuju javni kljuc
	// - Privatni kljucevi
	// - Tajni kljucevi, koji se koriste u simetricnima siframa
	private KeyStore keyStore;
	
	public KeyStoreReader() {
		try {
			keyStore = KeyStore.getInstance("JCEKS", "SunJCE");
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Ucitava privatni kljuc is KS fajla
	 */
	public  SecretKey readSecretKey(String keyStoreFile, String keyStorePass, String alias, String pass) {
		try {
			KeyStore ks = KeyStore.getInstance("JCEKS", "SunJCE");
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			ks.load(in, keyStorePass.toCharArray());
			
			if(ks.isKeyEntry(alias)) {
				SecretKey pk = (SecretKey) ks.getKey(alias, pass.toCharArray());
				return pk;
			}
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
}
