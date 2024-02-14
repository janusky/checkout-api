package checkoutapi.api;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import checkoutapi.entity.Role;
import checkoutapi.entity.RoleName;
import checkoutapi.entity.TokenRefresh;
import checkoutapi.entity.User;
import checkoutapi.payload.SignUpRequest;
import checkoutapi.security.CurrentUser;
import checkoutapi.security.JwtTokenProvider;
import checkoutapi.security.UserAuthPrincipal;
import checkoutapi.security.exception.AppException;
import checkoutapi.security.payload.AuthenticationLogin;
import checkoutapi.security.payload.AuthenticationSuccess;
import checkoutapi.security.payload.UserSummary;
import checkoutapi.security.repository.RoleRepository;
import checkoutapi.security.repository.TokenRefreshRepository;
import checkoutapi.security.repository.UserRepository;
import checkoutapi.security.service.UserService;
import checkoutapi.tools.ErrorHelper;
import checkoutapi.tools.ResponseDefault;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Authentication", description = "The Authentication API. Contains operations like change login, signup, logout, etc.")
public class AuthenticationApi {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	TokenRefreshRepository jwtRefreshTokenRepository;

	@Autowired
	AppException appException;

	@Autowired
	UserService userService;

	@Value("${app.jwt.expiration}")
	private long jwtExpirationInMs;

	/**
	 * 
	 * @param loginRequest {@link AuthenticationLogin}
	 * @return
	 */
	// @PostMapping(value = {"${app.route.authentication.login}", "/signin"})
	@Operation(summary = "User Authentication", description = "Authenticate the user and return a JWT token if the user is valid.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @io.swagger.v3.oas.annotations.media.Content(
    		mediaType = "application/json", examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
    		value = "{\n" + "  \"usernameOrEmail\": \"jane\",\n\"admin\": \"password\"\n" + "}", 
    		summary = "User Authentication Example")))
	@PostMapping(value = { "${app.route.auth.signin:/auth/login}", "/auth/signin" }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthenticationLogin loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserAuthPrincipal userAuth = (UserAuthPrincipal) authentication.getPrincipal();

		String accessToken = tokenProvider.generateToken(userAuth);
		String refreshToken = tokenProvider.refreshTokenId();

		saveRefreshToken(userAuth, refreshToken);

		return ResponseEntity.ok(new AuthenticationSuccess(accessToken, refreshToken, jwtExpirationInMs));
	}

	/**
	 * Register User.
	 * 
	 * @param signUpRequest {@link SignUpRequest}
	 * @return
	 */
	@PostMapping(value = { "${app.route.auth.signup:/auth/signup}" }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public ResponseEntity<?> registerPerson(@Valid @RequestBody SignUpRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return new ResponseEntity<ResponseDefault>(ErrorHelper.FAIL_USERNAME, HttpStatus.BAD_REQUEST);
		}
		String email = signUpRequest.getEmail();
		if (userRepository.existsByEmail(email)) {
			return new ResponseEntity<ResponseDefault>(ErrorHelper.FAIL_EMAIL, HttpStatus.BAD_REQUEST);
		}

		User user = new User(signUpRequest.getFullname(), signUpRequest.getUsername(), email,
				signUpRequest.getPassword());
		Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
				.orElseThrow(() -> appException.of("error.notfound", RoleName.ROLE_USER.name()));
		user.setRoles(Collections.singleton(userRole));

		String verifyCode = UUID.randomUUID().toString();
		user.setVerifyCode(verifyCode);

		userService.save(user);

		return ResponseEntity.ok(new ResponseDefault(true, "User registered successfully"));
	}

	@Operation(summary = "Get user details", 
		description = "Get the user details. The operation returns the details of the user that is associated "
			+ "with the provided JWT token.")
	@GetMapping({ "${app.route.auth.me:/auth/me}" })
	@PreAuthorize("isAuthenticated()")
	public UserSummary getCurrentUser(@CurrentUser UserAuthPrincipal currentUser) {
		UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(),
				currentUser.getName());
		return userSummary;
	}

	private void saveRefreshToken(UserAuthPrincipal userPrincipal, String refreshToken) {
		// Persist Refresh Token

		TokenRefresh jwtRefreshToken = new TokenRefresh(refreshToken);
		jwtRefreshToken.setUser(userRepository.getReferenceById(userPrincipal.getId()));

		Instant expirationDateTime = Instant.now().plus(360, ChronoUnit.DAYS); // Todo Add this in
																				// application.properties
		jwtRefreshToken.setExpirationDateTime(expirationDateTime);

		jwtRefreshTokenRepository.save(jwtRefreshToken);
	}
}
