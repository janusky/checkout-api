package checkoutapi.security;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import checkoutapi.security.exception.JwtExpiredTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProviderImpl.class);

	@Value("${app.jwt.secret}")
	private String jwtSecret;

	@Value("${app.jwt.expiration}")
	private long jwtExpirationInMs;

	public String generateToken(UserAuthPrincipal userPrincipal) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
		/*
		LocalDateTime currentTime = LocalDateTime.now();
		Date expiryDate = Date
				.from(currentTime.plusMinutes(jwtExpirationInMs).atZone(ZoneId.systemDefault()).toInstant());
		*/
		return Jwts.builder()
				.setSubject(Long.toString(userPrincipal.getId()))
				.setIssuedAt(now)
//				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.setExpiration(expiryDate)
				.compact();
	}
	
	public String refreshTokenId() {
		// generate a random UUID as refresh token
		return UUID.randomUUID().toString();
	}
	
	public Long getUserIdFromJWT(Jws<Claims> claimsJwt) {
		Claims body = claimsJwt.getBody();
		return Long.parseLong(body.getSubject());
	}

	public boolean validateToken(String token) {		
		parseClaims(jwtSecret, token);
		return true;
	}
	
	public Jws<Claims> parseClaims(String token) {
    	return parseClaims(jwtSecret, token);
    }
	
	/**
     * Parses and validates JWT Token signature.
     * 
     * @throws BadCredentialsException
     * @throws JwtExpiredTokenException
     * 
     */
    public Jws<Claims> parseClaims(String signingKey, String token) {
        try {
        	// return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
            return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException ex) {
            logger.warn("Invalid JWT Token", ex);
            throw new BadCredentialsException("Invalid JWT token: ", ex);
        } catch (ExpiredJwtException expiredEx) {
            logger.warn("JWT Token is expired", expiredEx);
            JwtToken jwt = () -> token;
            throw new JwtExpiredTokenException(jwt, "JWT Token expired", expiredEx);
        }
    }
    
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
