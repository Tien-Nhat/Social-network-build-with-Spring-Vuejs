package personalproject.social.network.response;

import personalproject.social.network.model.EFriendStatus;

public class FriendRequestResponse {
	private String avatar;
	private String firstName;
	private String lastName;
	private String content;
	private EFriendStatus status;
	private Long senderId;

	public EFriendStatus getStatus() {
		return status;
	}

	public void setStatus(EFriendStatus status) {
		this.status = status;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

}
