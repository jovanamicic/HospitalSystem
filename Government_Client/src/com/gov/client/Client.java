package com.gov.client;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class Client {
	
	public static void main(String[] args) {

		try {
			String keyPath = "C:\\Users\\katar\\git\\HospitalSystem\\KMJ.keystore";
			String keyPass = "kmjkmj";

			// path to SSL keystore
			System.setProperty("javax.net.ssl.keyStore", keyPath);
			System.setProperty("javax.net.ssl.keyStorePassword", keyPass);
			System.setProperty("javax.net.ssl.trustStore", keyPath);
			System.setProperty("javax.net.ssl.trustStorePassword", keyPass);
			//System.setProperty("javax.net.ssl.keyStoreType", keyType);

			// build the XML to post
			String xmlString = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:gs=\"com.government.model\"><soapenv:Header/> <soapenv:Body> <gs:getOperationsRequest> <gs:end_date>2017-05-21</gs:end_date></gs:getOperationsRequest> </soapenv:Body></soapenv:Envelope>";

			// post XML over HTTPS
			URL url = new URL("https://localhost:8081/ws"); // replace
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			connection.setRequestProperty("Content-Type", "text/xml");
			connection.setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			connection.connect();

			// tell the web server what we are sending
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write(xmlString);
			writer.flush();
			writer.close();

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

	}

}
