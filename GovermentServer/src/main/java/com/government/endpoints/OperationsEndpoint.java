package com.government.endpoints;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.commons.codec.binary.Base64;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.government.model.GetOperationsRequest;
import com.government.model.GetOperationsResponse;
import com.government.model.Operation;
import com.government.model.Report;

@Endpoint
public class OperationsEndpoint {
	
	private static final String NAMESPACE_URI = "com.government.model";
	private final String KEYSTORE_PATH = "../GOV_SERV.keystore";  
	private final String KEYSTORE_PASS = "govgov";
	private final String SECRET = "Neka nasa strasna tajna :D";
	

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getOperationsRequest")
	@ResponsePayload
	public GetOperationsResponse getOperation(@RequestPayload GetOperationsRequest request)  throws IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		GetOperationsResponse response = new GetOperationsResponse();
		List<Operation> operations = new ArrayList<>();

		try {

			// path to SSL keystore
			System.setProperty("javax.net.ssl.keyStore", KEYSTORE_PATH);
			System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASS);
			System.setProperty("javax.net.ssl.trustStore", KEYSTORE_PATH);
			System.setProperty("javax.net.ssl.trustStorePassword", KEYSTORE_PASS);
			
			String urlStr = "https://localhost:8082/government?";
			
			if (request.getName() != null)
				urlStr += "name=" + request.getName() + "&";
			
			if (request.getStartDate() != null)
				urlStr += "startDate=" + request.getStartDate() + "&";
			
			if (request.getEndDate() != null)
				urlStr += "endDate=" + request.getEndDate() + "&";
			
			URL url = new URL(urlStr); // replace
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setDoInput(true);

			connection.setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			
			KeyPair kp = getKeyPair();
			PrivateKey privateKey = kp.getPrivate();
			
			byte[] signature = sign(SECRET.getBytes(), privateKey);
			String signatureAsString = Base64.encodeBase64String(signature);
			connection.setRequestProperty("X-Signature", signatureAsString);
			
			connection.connect();
			
			// reading the response
			InputStreamReader reader = new InputStreamReader(connection.getInputStream());
			StringBuilder buf = new StringBuilder();
			char[] cbuf = new char[2048];
			int num;
			while (-1 != (num = reader.read(cbuf))) {
				buf.append(cbuf, 0, num);
			}
			String result = buf.toString();
			
			//convert JSON String to operation list
			operations = mapper.readValue(result, new TypeReference<List<Operation>>(){});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Report report = new Report();
		report.getOperation().addAll(operations);
		response.setReport(report);
		
		return response;
	}
	
	
	public KeyPair getKeyPair() throws Exception {
	    FileInputStream is = new FileInputStream(KEYSTORE_PATH);

	    KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
	    keystore.load(is, KEYSTORE_PASS.toCharArray());

	    String alias = "gov_serv";

	    Key key = keystore.getKey(alias, KEYSTORE_PASS.toCharArray());
	    
	    if (key instanceof PrivateKey) {
	      // Get certificate of public key
	      Certificate cert = keystore.getCertificate(alias);

	      // Get public key
	      PublicKey publicKey = cert.getPublicKey();

	      // Return a key pair
	      return new KeyPair(publicKey, (PrivateKey) key);
	    }
	    return null;
	  }
	
	private byte[] sign(byte[] data, PrivateKey privateKey) {
		try {
			Signature sig = Signature.getInstance("SHA1withRSA");
			sig.initSign(privateKey);
			sig.update(data);
			return sig.sign();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
