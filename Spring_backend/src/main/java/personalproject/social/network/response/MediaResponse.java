package personalproject.social.network.response;

public class MediaResponse {
	private Long id;
	private String description;
	private String base64String;
	private String contentType;

	public MediaResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MediaResponse(Long id, String description, String base64String, String contentType) {
		super();
		this.id = id;
		this.description = description;
		this.base64String = base64String;
		this.contentType = contentType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBase64String() {
		return base64String;
	}

	public void setBase64String(String base64String) {
		this.base64String = base64String;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
