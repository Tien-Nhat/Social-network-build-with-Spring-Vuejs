package personalproject.social.network.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import personalproject.social.network.request.NotificationRequest;
import personalproject.social.network.response.DataResponse;
import personalproject.social.network.services.NotificationService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/notifi")
public class NotificationController {

	@Autowired
	NotificationService notificationService;

	@PostMapping("/add")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse add(@RequestBody NotificationRequest notificationResponse) {
		return notificationService.addNotification(notificationResponse);
	}

	@GetMapping("/get")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse add() {
		return notificationService.getNotification();
	}

	@PostMapping("/set-is-read")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse setIsRead(@RequestParam Long notifiId) {
		return notificationService.setIsRead(notifiId);
	}
}
