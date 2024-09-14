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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import personalproject.social.network.request.PasswordRequest;
import personalproject.social.network.request.UserRequest;
import personalproject.social.network.response.DataResponse;
import personalproject.social.network.services.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping("/get")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse getUser(@RequestParam Long id) {
		return userService.getUser(id);
	}

	@PostMapping("/change-avatar")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse changeAvatar(MultipartHttpServletRequest data) {
		return userService.changeAvatar(data);
	}

	@PostMapping("/change-background")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse changeBackground(MultipartHttpServletRequest data) {
		return userService.changeBackground(data);
	}

	@PostMapping("/edit")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse editUser(@RequestBody UserRequest userRequest) {
		return userService.editUser(userRequest);
	}

	@PostMapping("/change-password")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse editUser(@RequestBody PasswordRequest password) {
		return userService.changePassword(password);
	}

	@GetMapping("/get-photo")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse getPhoto() {
		return userService.getMediaUser();
	}

	@GetMapping("/search-suggest-user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse searchSuggestUser(@RequestParam String txtSearch) {
		return userService.searchSuggestUser(txtSearch);
	}
}