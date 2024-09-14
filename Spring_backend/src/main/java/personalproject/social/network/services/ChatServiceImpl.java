package personalproject.social.network.services;

import java.io.File;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import personalproject.social.network.model.Chat;
import personalproject.social.network.model.User;
import personalproject.social.network.repository.ChatRepository;
import personalproject.social.network.repository.UserRepository;
import personalproject.social.network.request.ChatMessageRequest;
import personalproject.social.network.response.ChatHistoryResponse;
import personalproject.social.network.response.ChatMessageResponse;
import personalproject.social.network.response.ChatTextMediaResponse;
import personalproject.social.network.response.DataResponse;
import personalproject.social.network.util.Constant;
import personalproject.social.network.util.MessageCodes;
import personalproject.social.network.util.Utils;

@Service
public class ChatServiceImpl implements ChatService {
	private static final String URI_CHAT = "/chat/messages";
	private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	ChatRepository chatRepository;

	@Autowired
	AuthenticationFacade authenticationFacade;

	@Autowired
	Utils utils;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${post.media.path}")
	String uploadDir;

	@Autowired
	Environment env;

	@Override
	public DataResponse sendMessage(MultipartHttpServletRequest chatMessageRequest) {
		DataResponse res = new DataResponse();
		try {
			MultipartFile media = chatMessageRequest.getFile("media");
			String chatString = chatMessageRequest.getParameter("chatMessage");
			ChatMessageRequest chatMessage = objectMapper.readValue(chatString, ChatMessageRequest.class);
			Authentication authentication = authenticationFacade.getAuthentication();
			User receiver = userRepository.findById(chatMessage.getReceiverId()).get();
			User sender = userRepository.findByUsername(authentication.getName()).get();

			Chat chat = new Chat();
			chat.setMessage(chatMessage.getMessage());
			chat.setMessageDate(utils.getDateNow());
			chat.setReceiver(receiver);
			chat.setSender(sender);
			Chat chatSave = chatRepository.save(chat);

			ChatTextMediaResponse chatTextMediaResponse = new ChatTextMediaResponse();
			ChatMessageResponse chatResponse = new ChatMessageResponse();
			chatResponse.setSenderId(sender.getId());
			chatResponse.setDate(chatSave.getMessageDate());
			chatResponse.setMessage(chatSave.getMessage());
			chatResponse.setType(chatSave.getType());
			chatTextMediaResponse.setChatTextResponse(chatResponse);

			if (media != null) {
				String uniqueFileName = uploadDir + sender.getId() + "/message/" + receiver.getId()
						+ System.currentTimeMillis() + "." + media.getOriginalFilename();
				Path uploadPath = Paths.get(uniqueFileName);
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}
				File dest = new File(uniqueFileName);
				media.transferTo(dest);
				chat.setMessage(uniqueFileName);
				chat.setType(URLConnection.guessContentTypeFromName(uniqueFileName));
				chatRepository.save(chat);
				chatResponse.setMessage(utils.fileToBase64StringConversion(uniqueFileName));
				chatResponse.setType(URLConnection.guessContentTypeFromName(uniqueFileName));
				chatTextMediaResponse.setChatMediaResponse(chatResponse);
			}

			res.setResult(chatTextMediaResponse);
			res.setStatus(Constant.SUCCESS);
			res.setMessage(env.getProperty(MessageCodes.INFO_0006));
			simpMessagingTemplate.convertAndSendToUser(receiver.getUsername(), URI_CHAT, chatTextMediaResponse);
			simpMessagingTemplate.convertAndSendToUser(sender.getUsername(), URI_CHAT, chatTextMediaResponse);
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0004));
			logger.error(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse getHistoryChat(Long receiverId) {
		DataResponse res = new DataResponse();
		try {
			Authentication authentication = authenticationFacade.getAuthentication();
			User sender = userRepository.findByUsername(authentication.getName()).get();
			List<Chat> history = chatRepository.findChatsBySenderAndReceiverOrderByMessageDateAsc(sender.getId(),
					receiverId);
			List<ChatHistoryResponse> listChat = new ArrayList<>();
			for (Chat chat : history) {
				ChatHistoryResponse chatHistory = new ChatHistoryResponse();
				chatHistory.setDate(chat.getMessageDate());
				chatHistory.setMessage(chat.getMessage());
				chatHistory.setSenderId(chat.getSender().getId());
				chatHistory.setReceiverId(chat.getReceiver().getId());
				chatHistory.setType(chat.getType());
				listChat.add(chatHistory);
			}
			res.setResult(listChat);
			res.setStatus(Constant.SUCCESS);
			res.setMessage(env.getProperty(MessageCodes.INFO_0007));
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0005));
			logger.error(e.getMessage());
		}
		return res;
	}

}
