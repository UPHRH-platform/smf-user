package com.tarento.retail.config;

import static com.tarento.retail.util.Constants.JWT_GRANTED_AUTHORITY;
import static com.tarento.retail.util.Constants.JWT_ISSUER;
import static com.tarento.retail.util.Constants.SIGNING_KEY;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.tarento.retail.model.User;
import com.tarento.retail.util.AppConfiguration;
import com.tarento.retail.util.Constants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

	public static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String CLAIMS_KEY = "scopes";

	@Autowired
	AppConfiguration appConfig;

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(SIGNING_KEY).parseClaimsJws(token).getBody();
	}

	public Boolean isTokenExpired(String token) {
		try {
			final Date expiration = getExpirationDateFromToken(token);
			return expiration.before(new Date());
		} catch (Exception e) {
			LOGGER.error(String.format(Constants.EXCEPTION_METHOD, "isTokenExpired", e.getMessage()));
			return Boolean.TRUE;
		}
	}

	public String generateToken(User user) {
		return doGenerateToken(user.getUsername());
	}

	private String doGenerateToken(String subject) {

		Claims claims = Jwts.claims().setSubject(subject);
		claims.put(CLAIMS_KEY, Arrays.asList(new SimpleGrantedAuthority(JWT_GRANTED_AUTHORITY)));

		return Jwts.builder().setClaims(claims).setIssuer(JWT_ISSUER).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + appConfig.getJwtValidity() * 60 * 1000))
				.signWith(SignatureAlgorithm.HS256, SIGNING_KEY).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

}
