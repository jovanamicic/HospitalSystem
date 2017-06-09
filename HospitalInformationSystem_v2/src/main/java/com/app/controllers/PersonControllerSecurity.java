package com.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.PersonDTO;
import com.app.model.MyToken;
import com.app.security.TokenUtils;


@CrossOrigin
@RestController
public class PersonControllerSecurity {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	TokenUtils tokenUtils;

	/**
	 * @param personDTO
	 * @return
	 */
	@PreAuthorize("permitAll")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<MyToken> login(@RequestBody PersonDTO personDTO) {
		try {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(personDTO.getUsername(),
					personDTO.getPassword());
			Authentication authentication = authenticationManager.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			UserDetails details = userDetailsService.loadUserByUsername(personDTO.getUsername());
			return new ResponseEntity<>(new MyToken(tokenUtils.generateToken(details)), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(new MyToken("Invalid value"), HttpStatus.BAD_REQUEST);
		}
	}
}
