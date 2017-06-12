package com.goverment.endpoints;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.goverment.model.GetOperationsRequest;
import com.goverment.model.GetOperationsResponse;
import com.goverment.model.Operation;
import com.goverment.model.Report;
import com.goverment.service.OperationService;

@Endpoint
public class OperationsEndpoint {
	
	private static final String NAMESPACE_URI = "com.goverment.model";
	
	private final HttpClient client = HttpClientBuilder.create().build();

	private OperationService operationService;

	@Autowired
	public OperationsEndpoint(OperationService operationService) {
		this.operationService = operationService;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getOperationsRequest")
	@ResponsePayload
	public GetOperationsResponse getOperation(@RequestPayload GetOperationsRequest request)  throws IOException {
		GetOperationsResponse response = new GetOperationsResponse();
		System.out.println("aaaaaaaa");
//		HttpGet req = new HttpGet("https://localhost:8080/#/goverment");
//		
//		HttpResponse res = client.execute(req);
//		System.out.println("Response Code : "
//	                + res.getStatusLine().getStatusCode());
		
		try {
			String keyPath = "C:\\Users\\katar\\git\\HospitalSystem\\KMJ.keystore";
			String keyPass = "kmjkmj";
			

			// path to SSL keystore
			System.setProperty("javax.net.ssl.keyStore", keyPath);
			System.setProperty("javax.net.ssl.keyStorePassword", keyPass);
			System.setProperty("javax.net.ssl.trustStore", keyPath);
			System.setProperty("javax.net.ssl.trustStorePassword", keyPass);
			//System.setProperty("javax.net.ssl.keyStoreType", keyType);


			// post XML over HTTPS
			URL url = new URL("https://localhost:8080/goverment"); // replace
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);

			connection.setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
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
			System.out.println(result);
		} catch (Exception e) {
			System.out.println(e.getCause());
			e.printStackTrace();
		}
		
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd"); 
		
		String name = request.getName();
		String startDate = request.getStartDate();
		String endDate = request.getEndDate();
		
		if (name == null)
			name = "%"; //any name
		else
			name = "%" + name + "%";
		
		List<Operation> retVal = new ArrayList<>();
		
		//get all by name (if name is null, return all)
		if (startDate == null && endDate == null)  
			retVal = operationService.findByName(name);
		
		//get all by date between
		else if (startDate != null && endDate != null) 
			try {
				retVal = operationService.findByDate(dt.parse(startDate), dt.parse(endDate), name);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
		//get all after date
		else if (startDate != null && endDate == null) 
			try {
				retVal = operationService.findByDateAfter(dt.parse(startDate), name);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
		//get all before date
		else if (startDate == null && endDate != null) 
			try {
				retVal = operationService.findByDateBefore(dt.parse(endDate), name);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
		Report report = new Report();
		report.getOperation().addAll(retVal);
		response.setReport(report);
		
		return response;
	}

}
