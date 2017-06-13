import requests   # pip install requests[security]
import xml.dom.minidom

url="https://localhost:8081/ws"


body = """	<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
				xmlns:gs="com.goverment.model">
			   <soapenv:Header/>
			   <soapenv:Body>
				  <gs:getOperationsRequest>
					<gs:end_date>2017-05-21</gs:end_date>
				  </gs:getOperationsRequest>
			   </soapenv:Body>
			</soapenv:Envelope>
		"""

encoded_body = body.encode('utf-8')
		
headers = {'Content-Type': 'text/xml; charset=UTF-8'}

key_path = 'C:/Users/katar/Desktop/gov_cert/ca.key'
cer_path = 'C:/Users/katar/Desktop/gov_cert/ca.crt'
certificate = (cer_path, key_path)
response = requests.post(url = url,
						data =body,
						headers = headers,
						cert = certificate)


xml = xml.dom.minidom.parseString(response.content)
pretty_xml = xml.toprettyxml()
print(pretty_xml)