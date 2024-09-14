package personalproject.social.network.response;

import personalproject.social.network.model.EReportType;
import personalproject.social.network.model.EReportStatus;

public class ReportResponse {
	private Long reportId;
	private Long reporterId;
	private String reporterUsername;
	private Long reportedUserId;
	private String reportedUsername;
	private Long postId;
	private EReportStatus status;
	private EReportType reportType;
	private String reason;
	private Boolean postDeleted;
	private Boolean userBaned;

	public ReportResponse(Long reportId, Long reporterId, String reporterUsername, Long reportedUserId,
			String reportedUsername, Long postId, EReportStatus status, EReportType reportType, String reason,
			Boolean postDeleted, Boolean userBaned) {
		super();
		this.reportId = reportId;
		this.reporterId = reporterId;
		this.reporterUsername = reporterUsername;
		this.reportedUserId = reportedUserId;
		this.reportedUsername = reportedUsername;
		this.postId = postId;
		this.status = status;
		this.reportType = reportType;
		this.reason = reason;
		this.postDeleted = postDeleted;
		this.userBaned = userBaned;
	}

	public Long getReporterId() {
		return reporterId;
	}

	public void setReporterId(Long reporterId) {
		this.reporterId = reporterId;
	}

	public Long getReportedUserId() {
		return reportedUserId;
	}

	public void setReportedUserId(Long reportedUserId) {
		this.reportedUserId = reportedUserId;
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public String getReporterUsername() {
		return reporterUsername;
	}

	public void setReporterUsername(String reporterUsername) {
		this.reporterUsername = reporterUsername;
	}

	public String getReportedUsername() {
		return reportedUsername;
	}

	public void setReportedUsername(String reportedUsername) {
		this.reportedUsername = reportedUsername;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public EReportStatus getStatus() {
		return status;
	}

	public void setStatus(EReportStatus status) {
		this.status = status;
	}

	public EReportType getReportType() {
		return reportType;
	}

	public void setReportType(EReportType reportType) {
		this.reportType = reportType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Boolean getPostDeleted() {
		return postDeleted;
	}

	public void setPostDeleted(Boolean postDeleted) {
		this.postDeleted = postDeleted;
	}

	public Boolean getUserBaned() {
		return userBaned;
	}

	public void setUserBaned(Boolean userBaned) {
		this.userBaned = userBaned;
	}

}
