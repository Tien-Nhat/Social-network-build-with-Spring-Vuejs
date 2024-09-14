package personalproject.social.network.services;

import personalproject.social.network.request.LoginRequest;
import personalproject.social.network.request.SignupRequest;
import personalproject.social.network.response.DataResponse;

public interface AuthService {
	DataResponse login(LoginRequest loginRequest);

	DataResponse register(SignupRequest signUpRequest);

	DataResponse loginGoogle(String code, String type);

}
