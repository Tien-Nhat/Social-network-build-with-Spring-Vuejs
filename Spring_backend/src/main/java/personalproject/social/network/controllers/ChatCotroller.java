package personalproject.social.network.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import personalproject.social.network.response.DataResponse;
import personalproject.social.network.services.ChatService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/chat")
public class ChatCotroller {

	@Autowired
	ChatService chatService;

	@PostMapping("/send")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse sendMessage(MultipartHttpServletRequest chatMessageRequest) {
		return chatService.sendMessage(chatMessageRequest);
	}

	@GetMapping("/get-history")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse getHistory(@RequestParam Long receiverId) {
		return chatService.getHistoryChat(receiverId);
	}

}
