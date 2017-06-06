package com.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.stereotype.Component;


@Component
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String ROLE_MEDICAL_STUFF = "ROLE_MEDICAL_STUFF";
	private static final String ROLE_PATIENT = "ROLE_PATIENT";
	private static final String ROLE_GENERAL_MANAGER = "ROLE_GENERAL_MANAGER";
	private static final String ROLE_FINANCE_MANAGER = "ROLE_FINANCE_MANAGER";

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {

		authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public AuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
		AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
		authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
		return authenticationTokenFilter;
	}
	

//	@Override
//	protected void configure(HttpSecurity httpSecurity) throws Exception {
//		httpSecurity
//			.sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//				.and()
//			.authorizeRequests()
//			
//				.antMatchers("/**", "/login", "/persons/**").permitAll()
//				.antMatchers("/medicalstaff/**").access("hasRole('ROLE_MEDICAL_STUFF')")
//				//.antMatchers("/patients/**").hasAuthority("ROLE_PATIENT")
//				.antMatchers("/managers/**").access("hasRole('ROLE_MANAGER')").anyRequest().authenticated();
//				//if we use AngularJS on client side
//				//.and().csrf().csrfTokenRepository(csrfTokenRepository()); 
//		
//		//add filter for adding CSRF token in the request 
//		//httpSecurity.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);
//		
//		// Custom JWT based authentication
//		httpSecurity.addFilterBefore(authenticationTokenFilterBean(),
//				UsernamePasswordAuthenticationFilter.class);
//	}
	 @Override
	    protected void configure(HttpSecurity httpSecurity) throws Exception {

	        httpSecurity
	                .sessionManagement()
	                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	                .and()
	                .csrf().disable();

	        //httpSecurity.requiresChannel().anyRequest().requiresSecure();
	        httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
	    }
	
	/**
	 * If we use AngularJS as a client application, it will send CSRF token using 
	 * name X-XSRF token. We have to tell Spring to expect this name instead of 
	 * X-CSRF-TOKEN (which is default one)
	 * @return
	 */
//	private CsrfTokenRepository csrfTokenRepository() {
//		  HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
//		  repository.setHeaderName("X-XSRF-TOKEN");
//		  return repository;
//	}

}
