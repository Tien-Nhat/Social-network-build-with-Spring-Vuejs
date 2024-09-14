package personalproject.social.network.response;

public class UserLiteResponse {

	private Long userId;
	private String avatar;
	private String firstName;
	private String lastName;
	private String address;

	public UserLiteResponse(Long userId, String avatar, String firstName, String lastName) {
		super();
		this.userId = userId;
		this.avatar = avatar;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public UserLiteResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
