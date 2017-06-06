package com.app.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.model.Person;
import com.app.repository.PersonRepository;

@Service
public class PersonDetailsService implements UserDetailsService {

	@Autowired
	private PersonRepository personRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) {
		Person person = personRepository.findByUsername(username);

		if (person == null) {
			throw new UsernameNotFoundException(String.format("No person found with username '%s'.", username));
		} else {
			// Java 1.8 way
			List<GrantedAuthority> grantedAuthorities = person.getRoleMembers().stream()
					.map(authority -> new SimpleGrantedAuthority(authority.getRole().getName()))
					.collect(Collectors.toList());
			
			return new org.springframework.security.core.userdetails.User(person.getUsername(), person.getPassword(),
					grantedAuthorities);
		}
	}

}
