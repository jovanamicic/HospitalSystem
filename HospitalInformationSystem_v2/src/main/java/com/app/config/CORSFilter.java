//package com.app.config;
//import java.io.IOException;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CORSFilter implements Filter{
//    static Logger logger = LoggerFactory.getLogger(CORSFilter.class);
// 
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//    	//this method is empty
//    }
// 
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//       HttpServletResponse response = (HttpServletResponse) res;
//       
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, X-Auth-Token");
//        chain.doFilter(request, response);
//    }
// 
//    @Override
//    public void destroy() {
//    	//this method is empty
//    }
//}
//
