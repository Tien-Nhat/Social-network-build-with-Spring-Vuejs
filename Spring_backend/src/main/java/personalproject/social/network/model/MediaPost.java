package personalproject.social.network.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "media_post")
public class MediaPost {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "Media_Url")
	private String mediaUrl;

	@Column(name = "description")
	private String description;

	@Column(name = "content_Type")
	private String contentType;
	
	@Column(name="created_datetime")
	private String createdDatetime;
	
	@Column(name="deleted")
	private Boolean deleted=false;
	
	@ManyToOne
	@JoinColumn(name = "post_id", referencedColumnName = "id")
	@JsonIgnore
	private Post post;

	public MediaPost() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MediaPost(String mediaUrl, String description, String contentType, String createdDatetime,
			Post post) {
		super();
		this.mediaUrl = mediaUrl;
		this.description = description;
		this.contentType = contentType;
		this.createdDatetime = createdDatetime;
		this.post = post;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(String createdDatetime) {
		this.createdDatetime = createdDatetime;
	}
	
	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

}
