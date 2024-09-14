package personalproject.social.network.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import personalproject.social.network.request.ReportRequest;
import personalproject.social.network.response.DataResponse;
import personalproject.social.network.services.ReportService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/report")
public class ReportController {

	@Autowired
	ReportService reportService;

	@PostMapping("/add-report")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse addReport(@RequestBody ReportRequest reportRequest) {
		return reportService.addReport(reportRequest);
	}

	@GetMapping("/report-type")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse getReportType() {
		return reportService.getReportType();
	}
}
