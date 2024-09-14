package personalproject.social.network.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "social")
public class JwtConfig {
	private String jwtSecret;
	private int jwtExpirationMs;
	private String pathMedia;

	public String getJwtSecret() {
		return jwtSecret;
	}

	public void setJwtSecret(String jwtSecret) {
		this.jwtSecret = jwtSecret;
	}

	public int getJwtExpirationMs() {
		return jwtExpirationMs;
	}

	public void setJwtExpirationMs(int jwtExpirationMs) {
		this.jwtExpirationMs = jwtExpirationMs;
	}

	public String getPathMedia() {
		return pathMedia;
	}

	public void setPathMedia(String pathMedia) {
		this.pathMedia = pathMedia;
	}

}
