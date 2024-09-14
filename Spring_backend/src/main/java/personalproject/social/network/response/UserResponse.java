package personalproject.social.network.response;

import personalproject.social.network.model.EFriendStatus;

public class UserResponse {
	private String firstName;
	private String lastName;
	private String avatar;
	private String introduce;
	private String address;
	private String joined;
	private String phone;
	private int gender;
	private String birthday;
	private String email;
	private EFriendStatus friendStatus;
	private String background;

	public UserResponse() {
		super();
	}

	public UserResponse(String firstName, String lastName, String avatar, String introduce, String address,
			String joined, String phone, int gender, String birthday, String email, EFriendStatus friendStatus,
			String background) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.avatar = avatar;
		this.introduce = introduce;
		this.address = address;
		this.joined = joined;
		this.phone = phone;
		this.gender = gender;
		this.birthday = birthday;
		this.email = email;
		this.friendStatus = friendStatus;
		this.background = background;
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getJoined() {
		return joined;
	}

	public void setJoined(String joined) {
		this.joined = joined;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public EFriendStatus getFriendStatus() {
		return friendStatus;
	}

	public void setFriendStatus(EFriendStatus friendStatus) {
		this.friendStatus = friendStatus;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

}
