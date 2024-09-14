package personalproject.social.network.security.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import personalproject.social.network.config.JwtConfig;
import personalproject.social.network.error.ErrorResponse;
import personalproject.social.network.security.services.UserDetailsImpl;

@Component
public class JwtUtils {

	@Autowired
	JwtConfig jwtConfig;

	public String generateJwtToken(Authentication authentication) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtConfig.getJwtExpirationMs()))
				.signWith(key(), SignatureAlgorithm.HS256).compact();
	}

	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.getJwtSecret()));
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
	}

	public ErrorResponse validateJwtToken(String authToken) {
		ErrorResponse errorResponse = new ErrorResponse();
		try {
			Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
			errorResponse.setResult(true);

		} catch (Exception e) {
			errorResponse.setStatus(HttpStatus.UNAUTHORIZED);
			errorResponse.setMessage(e.getMessage());
			errorResponse.setResult(false);
		}

		return errorResponse;
	}
}
