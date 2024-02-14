package checkoutapi.confg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import checkoutapi.security.JwtAuthenticationEntryPoint;
import checkoutapi.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
	@Value("${app.route.actuator-uri:/actuator/**}")
	private String actuatorfUri;

	@Value("${app.route.path-public}")
	private List<String> appRoutePathPublic;

	@Value("${springdoc.path-dependencies}")
	private List<String> springDocsDependencies;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		appRoutePathPublic.addAll(springDocsDependencies);
		String[] permitAll = appRoutePathPublic.toArray(new String[0]);

		http
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(
				request -> request.requestMatchers(permitAll).permitAll()
					.anyRequest().authenticated()
			)
			.exceptionHandling(eH -> eH.defaultAuthenticationEntryPointFor(
					unauthorizedHandler, new AntPathRequestMatcher("/static/**")))
			.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authenticationProvider(authenticationProvider())
			.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
		return new JwtAuthenticationFilter();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
//	@Bean
//	public WebSecurityCustomizer webSecurityCustomizer() {
//		List<String> resourceStatic = Arrays.asList( 
//				"/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg",
//				"/**/*.html", "/**/*.css", "/**/*.js", "/**/*.ico");
//		List<String> pathIgnore = new ArrayList<String>();
//		pathIgnore.add(actuatorfUri);
//		pathIgnore.addAll(springDocsDependencies);
//		pathIgnore.addAll(appRoutePathPublic);
//		pathIgnore.addAll(resourceStatic);
//		String[] pathIgnoreList = pathIgnore.toArray(new String[0]);
//		return web -> web.ignoring().requestMatchers(pathIgnoreList);
//	}
}
