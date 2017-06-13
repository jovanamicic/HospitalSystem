package com.app.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddedTomcatConfiguration {
	
	 @Value("${server.additionalPorts}")
	    private String additionalPorts;

	    @Bean
	    public EmbeddedServletContainerFactory servletContainer() {
	        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
	        Connector[] additionalConnectors = this.additionalConnector();
	        if (additionalConnectors != null && additionalConnectors.length > 0) {
	            tomcat.addAdditionalTomcatConnectors(additionalConnectors);
	        }
	        return tomcat;
	    }

	    private Connector[] additionalConnector() {
	        //if (StringUtils.isBlank(this.additionalPorts)) {
	          //  return null;
	        //}
	        String[] ports = this.additionalPorts.split(",");
	        List<Connector> result = new ArrayList<>();
	        for (String port : ports) {
	            
	        	Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
	        	Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();

	        	protocol.setSSLEnabled(true);
	        	
	            connector.setScheme("https");
	            connector.setPort(Integer.valueOf(port));
	            connector.setSecure(true);
	            
	            protocol.setKeystoreFile("C:/Users/katar/git/HospitalSystem/GOV_SERV.keystore");
	            protocol.setKeystorePass("govgov");
	            
	            result.add(connector);
	        }
	        return result.toArray(new Connector[] {});
	    }

}
