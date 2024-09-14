package personalproject.social.network.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public class PostRequest {

	Long postId;

	@NotBlank
	Long userId;

	private String content;

	private List<Long> idDeleted;

	private Long sharedId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public List<Long> getIdDeleted() {
		return idDeleted;
	}

	public void setIdDeleted(List<Long> idDeleted) {
		this.idDeleted = idDeleted;
	}

	public Long getSharedId() {
		return sharedId;
	}

	public void setSharedId(Long sharedId) {
		this.sharedId = sharedId;
	}

}
