package com.gov.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Client {
	
	public static void main(String[] args) {

		try {
			String keyPath = "../GOV.keystore";
			String keyPass = "govgov";

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
			
			printFormattedXml(result);
			
			
		} catch (Exception e) {
			System.out.println(e.getCause());
			e.printStackTrace();
		}

	}

	private static void printFormattedXml(String xml) throws UnsupportedEncodingException, SAXException, IOException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
	
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		
		//initialize StreamResult with File object to save to file
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(doc);
		transformer.transform(source, result);
		String xmlString = result.getWriter().toString();
		System.out.println(xmlString);
		
	}

}
