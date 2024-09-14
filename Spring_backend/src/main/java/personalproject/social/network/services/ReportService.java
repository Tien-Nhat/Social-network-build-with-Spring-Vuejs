package personalproject.social.network.services;

import personalproject.social.network.request.ReportRequest;
import personalproject.social.network.response.DataResponse;

public interface ReportService {

	DataResponse addReport(ReportRequest reportRequest);

	DataResponse getReportType();

	DataResponse getListReport();

	DataResponse changeStatus(Long reportId, String status);
}
