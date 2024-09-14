package personalproject.social.network.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import personalproject.social.network.response.DataResponse;
import personalproject.social.network.services.FriendService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/friend")
public class FriendCotroller {

	@Autowired
	FriendService friendService;

	@PostMapping("/add-friend")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse setIsRead(@RequestParam Long receiverId) {
		return friendService.addFriend(receiverId);
	}

	@GetMapping("/get-map-status")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse getStatus() {
		return friendService.getMapStatus();
	}

	@GetMapping("/get-friend-request")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse getFriendRequest() {
		return friendService.getListFriendRequest();
	}

	@PostMapping("/accept-friend")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse acceptFriend(@RequestParam Long senderId) {
		return friendService.acceptRequest(senderId);
	}

	@GetMapping("/get-list-friend")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse getListFriend() {
		return friendService.getListFriend();
	}

	@DeleteMapping("/delete-friend")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse deleteFriend(@RequestParam Long friendId) {
		return friendService.deleteFriend(friendId);
	}

	@PostMapping("/change-status-friend")
	public DataResponse changeStatusFriend(@RequestParam Boolean isDisconnect,
			@RequestParam(required = false) Long userId) {
		return friendService.changeStatusUser(isDisconnect, userId);
	}
}
