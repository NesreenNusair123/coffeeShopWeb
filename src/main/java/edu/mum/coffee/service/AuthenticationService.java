package edu.mum.coffee.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import edu.mum.coffee.domain.Person;

@Component
public class AuthenticationService implements AuthenticationProvider {

	@Autowired
	private PersonService personService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		if(isAdmin(name, password)) {
			return adminAuth(name, password);
		}
		
		Person person = personService.login(name, password);
		
		if(person == null) {
			return null;
		}
		
		if(person.isAdmin()) {
			return adminAuth(name, password);
		}else {
			return personAuth(person.getEmail(), person.getPassword());
		}
	}

	private boolean isAdmin(String name, String password) {
		return name.equals("super") && password.equals("pw");
	}

	private Authentication adminAuth(String name, String password) {
		List<GrantedAuthority> grantedAuths = new ArrayList<>();
		grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
		grantedAuths.add(new SimpleGrantedAuthority("ADMIN"));
		Authentication auth = new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
		return auth;
	}

	private Authentication personAuth(String name, String password) {
		List<GrantedAuthority> grantedAuths = new ArrayList<>();
		grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
		Authentication auth = new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
		return auth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
