package personalproject.social.network.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import personalproject.social.network.response.DataResponse;
import personalproject.social.network.services.ReportService;
import personalproject.social.network.services.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	UserService userService;

	@Autowired
	ReportService reportService;

	@GetMapping("/get-list")
	@PreAuthorize("hasRole('ADMIN')")
	public DataResponse getListUser(@RequestParam int pageNumber, @RequestParam int pageSize) {
		return userService.getListUser(pageNumber, pageSize);
	}

	@PostMapping("/ban")
	@PreAuthorize("hasRole('ADMIN')")
	public DataResponse getListUser(@RequestParam Long userId) {
		return userService.banUser(userId);
	}

	@GetMapping("/get-report")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse getReport() {
		return reportService.getListReport();
	}

	@GetMapping("/search-user-by")
	@PreAuthorize("hasRole('ADMIN')")
	public DataResponse searchUserBy(@RequestParam String fieldName, @RequestParam String txtSearch,
			@RequestParam int pageNumber, @RequestParam int pageSize) {
		return userService.searchUserBy(fieldName, txtSearch, pageNumber, pageSize);
	}

	@PostMapping("/change-status")
	@PreAuthorize("hasRole('ADMIN')")
	public DataResponse changeStatus(@RequestParam Long reportId, @RequestParam String status) {
		return reportService.changeStatus(reportId, status);
	}
}
