package personalproject.social.network.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import personalproject.social.network.request.LoginRequest;
import personalproject.social.network.request.SignupRequest;
import personalproject.social.network.response.DataResponse;
import personalproject.social.network.services.AuthService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthService authService;

	@PostMapping("/signin")
	public DataResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		return authService.login(loginRequest);
	}

	@PostMapping("/signup")
	public DataResponse registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		return authService.register(signUpRequest);
	}

	@PostMapping("/oauth2/google")
	public DataResponse loginGoogle(@RequestParam String code, @RequestParam String type) {
		return authService.loginGoogle(code, type);
	}

}
