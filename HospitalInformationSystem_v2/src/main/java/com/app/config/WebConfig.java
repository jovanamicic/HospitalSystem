package com.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
//@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/**").allowedOrigins("http://localhost:8080")
//				.allowedMethods("GET", "PUT", "POST", "DELETE", "OPTIONS").exposedHeaders("Access-Control-Allow-Origin");
//	}
	
	@Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new PortInterceptor()).addPathPatterns("/**");
    }
}
