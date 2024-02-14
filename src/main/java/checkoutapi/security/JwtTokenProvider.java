package checkoutapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public interface JwtTokenProvider {
	public String generateToken(UserAuthPrincipal userPrincipal);

	/**
	 * Devuelve un identificador de token refresh.
	 * 
	 * @return String
	 */
	public String refreshTokenId();

	public Long getUserIdFromJWT(Jws<Claims> claimsJwt);
	
	public boolean validateToken(String token);
	
	/**
	 * NOTA: Realiza validación al parsear el token.
	 * 
	 * @param token
	 * @return
	 */
	public Jws<Claims> parseClaims(String token);
}
