package personalproject.social.network.websocket;

import java.security.Principal;

public class UserSocket implements Principal {

	private String name;

	public UserSocket(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

}
