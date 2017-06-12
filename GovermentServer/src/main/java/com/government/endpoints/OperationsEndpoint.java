package com.government.endpoints;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

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
	

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getOperationsRequest")
	@ResponsePayload
	public GetOperationsResponse getOperation(@RequestPayload GetOperationsRequest request)  throws IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		GetOperationsResponse response = new GetOperationsResponse();
		List<Operation> operations = new ArrayList<>();

		try {
			String keyPath = "../KMJ.keystore";
			String keyPass = "kmjkmj";
			
//			String keyPath = "../GOV_SERV.keystore";  zasto kad je sertifikat importovan u truststore?
//			String keyPass = "govgov";

			// path to SSL keystore
			System.setProperty("javax.net.ssl.keyStore", keyPath);
			System.setProperty("javax.net.ssl.keyStorePassword", keyPass);
			System.setProperty("javax.net.ssl.trustStore", keyPath);
			System.setProperty("javax.net.ssl.trustStorePassword", keyPass);
			
			String urlStr = "https://localhost:8080/government?";
			
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
			
			connection.connect();
			
//			System.out.println(connection.getServerCertificates()[0].toString());  vidi samo KMJ sertifikat!

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

}
