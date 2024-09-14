package personalproject.social.network.request;

import jakarta.validation.constraints.NotBlank;

public class CommentsRequest {

	@NotBlank
	private Long postId;

	@NotBlank
	private String commentText;

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

}
