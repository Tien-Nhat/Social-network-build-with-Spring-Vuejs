package personalproject.social.network.error;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import personalproject.social.network.security.jwt.JwtUtils;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice()
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	Environment env;

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<Object> handleSQLIntergrityException(HttpRequest req,
			SQLIntegrityConstraintViolationException ex) {
		String error = "Unable to submit post: " + ex.getMessage();
		return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, error));
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<Object> handleNoSuchElementExceptione(HttpServletRequest req, NoSuchElementException ex) {
		ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND);
		response.setMessage("the row for address is not existent: " + req.getRequestURI());
		return buildResponseEntity(response);
	}

	private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse) {
		return new ResponseEntity<Object>(errorResponse, errorResponse.getStatus());
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, HttpServletRequest req)
			throws IOException {
		ErrorResponse response = new ErrorResponse(HttpStatus.UNAUTHORIZED);
		String jwt = parseJwt(req);
		response = jwtUtils.validateJwtToken(jwt);
		return buildResponseEntity(response);
	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7);
		}

		return null;
	}

}
