package personalproject.social.network.request;

public class ReportRequest {

	private Long reportTypeId;
	private Long postId;
	private String reason;

	public Long getReportTypeId() {
		return reportTypeId;
	}

	public void setReportTypeId(Long reportTypeId) {
		this.reportTypeId = reportTypeId;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
