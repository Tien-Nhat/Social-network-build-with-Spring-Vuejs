package personalproject.social.network.response;

public class NotificationLiteResponse {
	private Long userId;
	private Long notifiId;
	private String avatar;
	private String firstName;
	private String lastName;
	private String notification;
	private Boolean isRead;
	private Long postId;
	private String notifiDate;

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	public Long getUserId() {
		return userId;
	}

	public Long getNotifiId() {
		return notifiId;
	}

	public void setNotifiId(Long notifiId) {
		this.notifiId = notifiId;
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

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public String getNotifiDate() {
		return notifiDate;
	}

	public void setNotifiDate(String notifiDate) {
		this.notifiDate = notifiDate;
	}

}
