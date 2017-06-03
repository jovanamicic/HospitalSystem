package com.app.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenUtils {

	@Value("myXAuthSecret")
	private String secret;

	@Value("18000") // in seconds (5 hours)
	private Long expiration;

	public String getUsernameFromToken(String token) {
		String username = null;
		try {
			Claims claims = this.getClaimsFromToken(token);
			if (claims != null)
				username = claims.getSubject();
		} catch (Exception e) {
			username = null;
		}
		return username;
	}

	private Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	public Date getExpirationDateFromToken(String token) {
		Date exp = null;
		try {
			final Claims claims = this.getClaimsFromToken(token);
			if (claims != null)
				exp = claims.getExpiration();
		} catch (Exception e) {
			exp = null;
		}
		return exp;
	}

	public boolean isTokenExpired(String token) {
		final Date exp = this.getExpirationDateFromToken(token);
		return exp.before(new Date(System.currentTimeMillis()));
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("sub", userDetails.getUsername());
		claims.put("created", new Date(System.currentTimeMillis()));
		return Jwts.builder().setClaims(claims).setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

}
