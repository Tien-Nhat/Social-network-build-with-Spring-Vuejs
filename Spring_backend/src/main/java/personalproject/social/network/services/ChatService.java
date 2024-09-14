package personalproject.social.network.services;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import personalproject.social.network.response.DataResponse;

public interface ChatService {

	DataResponse sendMessage(MultipartHttpServletRequest chatMessageRequest);

	DataResponse getHistoryChat(Long receiverId);

}
