package com.government.config;

import java.util.List;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {
	
	@Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        PayloadValidatingInterceptor validatingInterceptor = new PayloadValidatingInterceptor();
        validatingInterceptor.setValidateRequest(true);
        validatingInterceptor.setValidateResponse(true);
        validatingInterceptor.setXsdSchema(xmlSchemaReport());
        interceptors.add(validatingInterceptor);
    }

	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(servlet, "/government/*");
	}

	@Bean(name = "operations")
	public DefaultWsdl11Definition operationsWsdl11Definition(XsdSchema xmlSchemaOperations) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("OperationsPort");
		wsdl11Definition.setLocationUri("/government");
		wsdl11Definition.setTargetNamespace("com.government.model");
		wsdl11Definition.setSchema(xmlSchemaOperations);
		return wsdl11Definition;
	}
	
	@Bean(name = "examinations")
	public DefaultWsdl11Definition examinationsWsdl11Definition(XsdSchema xmlSchemaExaminations) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("ExaminationsPort");
		wsdl11Definition.setLocationUri("/government");
		wsdl11Definition.setTargetNamespace("com.government.model");
		wsdl11Definition.setSchema(xmlSchemaExaminations);
		return wsdl11Definition;
	}

	@Bean
	public XsdSchema xmlSchemaReport() {
		return new SimpleXsdSchema(new ClassPathResource("/xsd/government_report.xsd"));
	}

}
