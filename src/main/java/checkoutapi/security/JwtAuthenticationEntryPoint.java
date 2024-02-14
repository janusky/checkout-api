package checkoutapi.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import checkoutapi.security.exception.AuthMethodNotSupportedException;
import checkoutapi.security.exception.JwtExpiredTokenException;
import checkoutapi.security.payload.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	//private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
	private enum Errors {
		AUTHENTICATION, AUTHENTICATION_CREDENTIAL, AUTHENTICATION_METODH_SUPPORT, JWT_TOKEN_EXPIRED
	};
    
    @Autowired
    private ObjectMapper mapper;
    
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) throws IOException, ServletException {    	
    	onAuthenticationFailure(request, response, e);
    }    
    
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {
		
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		Object eRequest = request.getAttribute(jakarta.servlet.RequestDispatcher.ERROR_EXCEPTION);
		
		if (e instanceof BadCredentialsException || eRequest instanceof BadCredentialsException) {
			mapper.writeValue(response.getWriter(), ErrorResponse.of("Invalid username or password",
					Errors.AUTHENTICATION_CREDENTIAL.toString(), HttpStatus.UNAUTHORIZED));
		} else if (e instanceof JwtExpiredTokenException || eRequest instanceof JwtExpiredTokenException) {
			mapper.writeValue(response.getWriter(), ErrorResponse.of("Token has expired",
					Errors.JWT_TOKEN_EXPIRED.toString(), HttpStatus.UNAUTHORIZED));
		} else if (e instanceof AuthMethodNotSupportedException || eRequest instanceof AuthMethodNotSupportedException) {
			mapper.writeValue(response.getWriter(), ErrorResponse.of(e.getMessage(),
					Errors.AUTHENTICATION_METODH_SUPPORT.toString(), HttpStatus.UNAUTHORIZED));
		} else {			
			mapper.writeValue(response.getWriter(), ErrorResponse.of("Authentication failed",
					Errors.AUTHENTICATION.toString(), HttpStatus.UNAUTHORIZED));
		}
	}
}
