package personalproject.social.network.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import personalproject.social.network.model.Report;
import personalproject.social.network.response.ReportResponse;

public interface ReportRepository extends JpaRepository<Report, Long> {
	@Query("SELECT new personalproject.social.network.response.ReportResponse"
			+ "(r.id,r.user.id, r.user.username,r.post.user.id, r.post.user.username,r.post.id, r.status, r.reportType.name,r.reason,r.post.deleted,r.post.user.banned) FROM Report r")
	List<ReportResponse> findAllReportResponses();
}
