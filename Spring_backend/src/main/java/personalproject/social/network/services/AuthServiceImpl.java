package personalproject.social.network.services;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import personalproject.social.network.config.GoogleConfig;
import personalproject.social.network.config.JwtConfig;
import personalproject.social.network.model.ERole;
import personalproject.social.network.model.Role;
import personalproject.social.network.model.User;
import personalproject.social.network.repository.RoleRepository;
import personalproject.social.network.repository.UserRepository;
import personalproject.social.network.request.LoginRequest;
import personalproject.social.network.request.SignupRequest;
import personalproject.social.network.response.DataResponse;
import personalproject.social.network.response.JwtResponse;
import personalproject.social.network.security.jwt.JwtUtils;
import personalproject.social.network.security.services.UserDetailsImpl;
import personalproject.social.network.util.Constant;
import personalproject.social.network.util.MessageCodes;
import personalproject.social.network.util.Utils;

@Service
public class AuthServiceImpl implements AuthService {

	private static final String GOOGLE_TOKEN_API = "https://oauth2.googleapis.com/token";
	private static final String GOOGLE_USER_INFO_API_V1 = "https://www.googleapis.com/oauth2/v1/userinfo";

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	Environment env;

	@Value("${post.media.path}")
	String uploadDir;

	@Autowired
	Utils utils;

	@Autowired
	GoogleConfig googleConfig;

	@Autowired
	JwtConfig jwtConfig;

	@Override
	public DataResponse login(LoginRequest loginRequest) {
		DataResponse res = new DataResponse();
		try {

			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			String jwt = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());

			LocalDateTime instance = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			res.setStatus(HttpStatus.OK.name());
			res.setMessage(env.getProperty(MessageCodes.INFO_0004));
			res.setResult(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getId(),
					formatter.format(instance), jwtConfig.getJwtExpirationMs(), roles));
		} catch (Exception e) {
			res.setStatus(HttpStatus.UNAUTHORIZED.name());
			res.setMessage(Constant.ERROR);
			if (!userRepository.existsByUsername(loginRequest.getUsername())) {
				res.setStatus(HttpStatus.UNAUTHORIZED.name());
				res.setMessage(env.getProperty(MessageCodes.WARN_0006));
				return res;
			} else {
				User user = userRepository.findByUsername(loginRequest.getUsername()).get();
				if (user.getBanned()) {
					res.setStatus(HttpStatus.UNAUTHORIZED.name());
					res.setMessage(env.getProperty(MessageCodes.WARN_0002));
				} else if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
					res.setStatus(HttpStatus.UNAUTHORIZED.name());
					res.setMessage(env.getProperty(MessageCodes.ERROR_0002));
					return res;
				}
			}
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public DataResponse register(SignupRequest signUpRequest) {
		DataResponse res = new DataResponse();
		try {
			if (userRepository.existsByUsername(signUpRequest.getUsername())) {
				res.setStatus(HttpStatus.UNAUTHORIZED.name());
				res.setMessage(env.getProperty(MessageCodes.WARN_0004));
				return res;
			}

			if (userRepository.existsByEmail(signUpRequest.getEmail())) {
				res.setStatus(HttpStatus.UNAUTHORIZED.name());
				res.setMessage(env.getProperty(MessageCodes.WARN_0005));
				return res;
			}

			LocalDateTime instance = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constant.FORMAT_DATE);

			// Create new user's account
			User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
					encoder.encode(signUpRequest.getPassword()), signUpRequest.getFirstname(),
					signUpRequest.getLastname(), signUpRequest.getBirthday(), signUpRequest.getPhone(),
					signUpRequest.getGender(), formatter.format(instance), signUpRequest.getAddress());

			Set<String> strRoles = signUpRequest.getRole();
			Set<Role> roles = new HashSet<>();

			if (strRoles == null) {
				Role userRole = roleRepository.findByName(ERole.ROLE_USER)
						.orElseThrow(() -> new RuntimeException(env.getProperty(MessageCodes.ERROR_0003)));
				roles.add(userRole);
			} else {
				strRoles.forEach(role -> {
					switch (role) {
					case "admin":
						Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException(env.getProperty(MessageCodes.ERROR_0003)));
						roles.add(adminRole);

						break;
					case "mod":
						Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
								.orElseThrow(() -> new RuntimeException(env.getProperty(MessageCodes.ERROR_0003)));
						roles.add(modRole);

						break;
					default:
						Role userRole = roleRepository.findByName(ERole.ROLE_USER)
								.orElseThrow(() -> new RuntimeException(env.getProperty(MessageCodes.ERROR_0003)));
						roles.add(userRole);
					}
				});
			}

			user.setRoles(roles);
			userRepository.save(user);
			res.setStatus(HttpStatus.OK.name());
			res.setMessage(env.getProperty(MessageCodes.INFO_0005));
		} catch (Exception e) {
			res.setStatus(HttpStatus.UNAUTHORIZED.name());
			res.setMessage(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse loginGoogle(String code, String type) {
		DataResponse res = new DataResponse();
		try {

			Map<String, Object> userInfo;
			Boolean verifiedEmail;
			if (type.equals("code")) {
				String accessToken = getAccessTokenGoogle(code);
				userInfo = getUserInfo(accessToken);
				verifiedEmail = (Boolean) userInfo.get("verified_email");
			} else {

				String[] chunks = code.split("\\.");
				Base64.Decoder decoder = Base64.getUrlDecoder();
				String payload = new String(decoder.decode(chunks[1]));
				ObjectMapper objectMapper = new ObjectMapper();
				userInfo = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {
				});
				verifiedEmail = (Boolean) userInfo.get("email_verified");
			}

			if (verifiedEmail == true) {

				Optional<User> optionalUser = userRepository.findByEmail((String) userInfo.get("email"));
				User user = new User();
				if (optionalUser.isPresent()) {
					user = optionalUser.get();
					if (user.getBanned()) {
						res.setStatus(HttpStatus.UNAUTHORIZED.name());
						res.setMessage(env.getProperty(MessageCodes.WARN_0002));
						return res;
					}
				} else {
					user.setUsername((String) userInfo.get("email"));
					user.setEmail((String) userInfo.get("email"));
					user.setFirstname((String) userInfo.get("family_name"));
					user.setLastname((String) userInfo.get("given_name"));
					user.setJoined(utils.getDateNow());
					user.setPassword(encoder.encode(""));

					Set<Role> roles = new HashSet<>();
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException(env.getProperty(MessageCodes.ERROR_0003)));
					roles.add(userRole);
					user.setRoles(roles);
					User newUser = userRepository.save(user);
					String avatarPath = saveAvatarGoogle((String) userInfo.get("picture"), newUser.getId());
					newUser.setAvatar(avatarPath);
					userRepository.save(newUser);
				}

				Authentication authentication = authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), ""));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				String jwt = jwtUtils.generateJwtToken(authentication);

				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
						.collect(Collectors.toList());

				LocalDateTime instance = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

				res.setStatus(HttpStatus.OK.name());
				res.setMessage(env.getProperty(MessageCodes.INFO_0004));
				res.setResult(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getId(),
						formatter.format(instance), jwtConfig.getJwtExpirationMs(), roles));

			}

		} catch (Exception e) {
			res.setStatus(HttpStatus.UNAUTHORIZED.name());
			res.setMessage(Constant.ERROR);
		}
		return res;
	}

	public Map<String, Object> getUserInfo(String accessToken) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<>(headers);

		ResponseEntity<Map<String, Object>> response = restTemplate.exchange(GOOGLE_USER_INFO_API_V1, HttpMethod.GET,
				request, new ParameterizedTypeReference<Map<String, Object>>() {
				});

		// Check if the response was successful
		if (response.getStatusCode() == HttpStatus.OK) {
			return response.getBody();
		} else {
			// Handle error cases
			throw new RuntimeException("Failed to fetch user info. Status code: " + response.getStatusCode());
		}
	}

	public String saveAvatarGoogle(String imageUrl, Long userId) throws Exception {
		URL url = new URL(imageUrl);

		try (InputStream in = url.openStream()) {

			String fileName = System.currentTimeMillis() + "." + Paths.get(url.getPath()).getFileName().toString();
			String destinationDirectory = uploadDir + userId + "/avatar/";

			Path destinationPath = Paths.get(destinationDirectory, fileName);

			Files.createDirectories(destinationPath.getParent());

			Files.copy(in, destinationPath);

			return destinationPath.toString();
		} catch (Exception e) {
			System.out.println("Failed to download image: " + e.getMessage());
			throw e;
		}
	}

	public String getAccessTokenGoogle(String authCode) throws Exception {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			Map<String, String> params = new HashMap<>();
			params.put("code", authCode);
			params.put("client_id", googleConfig.getClientId());
			params.put("client_secret", googleConfig.getClientSecret());
			params.put("redirect_uri", googleConfig.getRedirectUri());
			params.put("grant_type", "authorization_code");

			HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);
			ResponseEntity<Map<String, Object>> response = restTemplate.exchange(GOOGLE_TOKEN_API, HttpMethod.POST,
					request, new ParameterizedTypeReference<Map<String, Object>>() {
					});
			Map<String, Object> responseBody = response.getBody();

			String accessToken = (String) responseBody.get("access_token");

			return accessToken;
		} catch (Exception e) {
			System.out.println("Failed to download image: " + e.getMessage());
			throw e;
		}

	}

}
