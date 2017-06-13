package com.app.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class PortInterceptor extends HandlerInterceptorAdapter {
    
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
    	String path = request.getRequestURI().substring(1);
    	
    	if(request.getLocalPort() == 8082) {
    		if (path.equals("government"))
    			return true;
    		return false;
    	}
    	
    	if(request.getLocalPort() == 8080) {
    		if (path.equals("government"))
    			return false;
    		return true;
    	}
    	
        return true;
    }
}