package personalproject.social.network.response;

import java.util.List;

import personalproject.social.network.model.EFriendStatus;

public class PostResponse {

	private Long id;
	private String content;
	private List<MediaResponse> medias;
	private Long userId;
	private String introduce;
	private EFriendStatus friendStatus;
	private String firstName;
	private String lastName;
	private String createdDatetime;
	private String avatar;
	private Boolean liked = false;
	private List<UserLiteResponse> listLiked;
	private List<CommentsResponse> listComment;
	private PostSharedResponse postShare;

	public PostResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EFriendStatus getFriendStatus() {
		return friendStatus;
	}

	public void setFriendStatus(EFriendStatus friendStatus) {
		this.friendStatus = friendStatus;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<MediaResponse> getMedias() {
		return medias;
	}

	public void setMedias(List<MediaResponse> medias) {
		this.medias = medias;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
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

	public String getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(String createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getLiked() {
		return liked;
	}

	public void setLiked(Boolean liked) {
		this.liked = liked;
	}

	public List<UserLiteResponse> getListLiked() {
		return listLiked;
	}

	public void setListLiked(List<UserLiteResponse> listLiked) {
		this.listLiked = listLiked;
	}

	public List<CommentsResponse> getListComment() {
		return listComment;
	}

	public void setListComment(List<CommentsResponse> listComment) {
		this.listComment = listComment;
	}

	public PostSharedResponse getPostShare() {
		return postShare;
	}

	public void setPostShare(PostSharedResponse postShare) {
		this.postShare = postShare;
	}

}
