package checkoutapi.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * <p>Si recibe un token realiza el chequeo de su estado.</p>
 * <p>Por defecto siempre intenta ejecutar la petición solicitada.</p>
 * 
 * @author janusky@mail.com
 * @version 1.0 - Aug 28, 2018 8:32:24 AM
 *
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	// private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);	
	public static String TOEKN_HEADER_PREFIX = "Bearer ";
	
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    
    @Value("${app.jwt.header}")
    private String tokenHeader;
    
    public JwtAuthenticationFilter() {
	}
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
    	String jwtToken = getJwtFromRequest(request);

        if (StringUtils.hasText(jwtToken)) {
			Jws<Claims> claimsJwt = tokenProvider.parseClaims(jwtToken);
            Long userId = tokenProvider.getUserIdFromJWT(claimsJwt);

            /*
	            Note that you could also encode the user's username and roles inside JWT claims
	            and create the UserDetails object by parsing those claims from the JWT.
	            That would avoid the following database hit. It's completely up to you.
	         */
	        UserDetails userDetails = userDetailsService.loadUserById(userId);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
	        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	
	        SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
	}

    private String getJwtFromRequest(HttpServletRequest request) {    	
    	String bearerToken = request.getHeader(tokenHeader);
        String token = null;
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOEKN_HEADER_PREFIX)) {            
            token = bearerToken.substring(TOEKN_HEADER_PREFIX.length(), bearerToken.length());
        }
        return token;    	
    }
}
