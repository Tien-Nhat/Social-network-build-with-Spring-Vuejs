package personalproject.social.network.response;

import java.util.List;

public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private Long id;
	private String username;
	private String createdDatetime;
	private int jwtExpirationMs;
	private List<String> roles;

	public JwtResponse(String accessToken, String username, Long id, String createdDatetime, int jwtExpirationMs,
			List<String> roles) {
		this.token = accessToken;
		this.username = username;
		this.id = id;
		this.createdDatetime = createdDatetime;
		this.jwtExpirationMs = jwtExpirationMs;
		this.roles = roles;
	}

	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getJwtExpirationMs() {
		return jwtExpirationMs;
	}

	public void setJwtExpirationMs(int jwtExpirationMs) {
		this.jwtExpirationMs = jwtExpirationMs;
	}

	public String getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(String createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public List<String> getRoles() {
		return roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
