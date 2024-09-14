package personalproject.social.network.response;

public class CommentsResponse {
	private Long userId;
	private String avatar;
	private String commentDate;
	private String commentText;
	private String firstName;
	private String lastName;
	private String introduce;

	public CommentsResponse(Long userId, String avatar, String commentDate, String commentText, String firstName,
			String lastName, String introduce) {
		super();
		this.userId = userId;
		this.avatar = avatar;
		this.commentDate = commentDate;
		this.commentText = commentText;
		this.firstName = firstName;
		this.lastName = lastName;
		this.introduce = introduce;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
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

	public String getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
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

}
