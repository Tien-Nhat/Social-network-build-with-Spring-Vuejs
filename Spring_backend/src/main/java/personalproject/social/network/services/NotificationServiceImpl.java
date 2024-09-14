package personalproject.social.network.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import personalproject.social.network.model.Notification;
import personalproject.social.network.model.User;
import personalproject.social.network.repository.NotificationRepository;
import personalproject.social.network.repository.PostRepository;
import personalproject.social.network.repository.UserRepository;
import personalproject.social.network.request.NotificationRequest;
import personalproject.social.network.response.DataResponse;
import personalproject.social.network.response.NotificationLiteResponse;
import personalproject.social.network.response.NotificationResponse;
import personalproject.social.network.util.Constant;
import personalproject.social.network.util.MessageCodes;
import personalproject.social.network.util.Utils;

@Service
public class NotificationServiceImpl implements NotificationService {

	private static final String URI_NOTIFI = "/notifi/messages";
	private static final String COMMENT = "comment";
	private static final String LIKE = "like";
	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

	@Autowired
	NotificationRepository notificationRepository;

	@Autowired
	AuthenticationFacade authenticationFacade;

	@Autowired
	Utils utils;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PostRepository postRepository;

	@Autowired
	Environment env;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Override
	public DataResponse addNotification(NotificationRequest notificationRequest) {
		DataResponse res = new DataResponse();
		try {
			Authentication authentication = authenticationFacade.getAuthentication();

			User userSender = userRepository.findByUsername(authentication.getName()).get();
			User userReceiver = userRepository.findById(notificationRequest.getReceiverId()).get();

			Notification notification = new Notification();
			String content = Constant.EMPTY;

			switch (notificationRequest.getType()) {
			case COMMENT:
				content = env.getProperty(MessageCodes.INFO_0022);
				break;
			case LIKE:
				content = env.getProperty(MessageCodes.INFO_0023);
				break;
			default:
				break;
			}

			notification.setContent(content);
			notification.setSender(userSender);
			notification.setReceiver(userReceiver);
			notification.setNotificationDate(utils.getDateNow());
			notification.setPost(postRepository.findById(notificationRequest.getPostId()).get());
			Notification notifi = notificationRepository.save(notification);

			NotificationLiteResponse notifiResponse = new NotificationLiteResponse();
			notifiResponse.setAvatar(
					(userSender.getAvatar() != null) ? utils.fileToBase64StringConversion(userSender.getAvatar())
							: null);
			notifiResponse.setFirstName(userSender.getFirstname());
			notifiResponse.setLastName(userSender.getLastname());
			notifiResponse.setNotification(content);
			notifiResponse.setIsRead(false);
			notifiResponse.setNotifiId(notifi.getId());
			notifiResponse.setPostId(notificationRequest.getPostId());
			notifiResponse.setNotifiDate(notifi.getNotificationDate());
			simpMessagingTemplate.convertAndSendToUser(userReceiver.getUsername(), URI_NOTIFI, notifiResponse);
			res.setStatus(Constant.SUCCESS);
			res.setMessage(env.getProperty(MessageCodes.INFO_0019));
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0001));
			logger.error(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse getNotification() {
		DataResponse res = new DataResponse();
		try {
			Authentication authentication = authenticationFacade.getAuthentication();
			User userReceiver = userRepository.findByUsername(authentication.getName()).get();
			List<Notification> listNotifi = notificationRepository
					.findByReceiver_IdOrderByNotificationDateDesc(userReceiver.getId());
			NotificationResponse notificationResponse = new NotificationResponse();
			List<NotificationLiteResponse> listNotifiResponse = new ArrayList<>();

			for (Notification notifi : listNotifi) {
				NotificationLiteResponse notifiResponse = new NotificationLiteResponse();
				User userSender = notifi.getSender();
				notifiResponse.setAvatar(
						(userSender.getAvatar() != null) ? utils.fileToBase64StringConversion(userSender.getAvatar())
								: null);
				notifiResponse.setFirstName(userSender.getFirstname());
				notifiResponse.setLastName(userSender.getLastname());
				notifiResponse.setNotification(notifi.getContent());
				notifiResponse.setIsRead(notifi.getIsRead());
				notifiResponse.setNotifiId(notifi.getId());
				notifiResponse.setPostId(notifi.getPost().getId());
				notifiResponse.setNotifiDate(notifi.getNotificationDate());
				listNotifiResponse.add(notifiResponse);
			}
			notificationResponse
					.setCountNotRead(notificationRepository.countByReceiver_IdAndIsReadFalse(userReceiver.getId()));
			notificationResponse.setListNotifi(listNotifiResponse);
			res.setResult(notificationResponse);
			res.setStatus(Constant.SUCCESS);
			res.setMessage(env.getProperty(MessageCodes.INFO_0020));
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0001));
			logger.error(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse setIsRead(Long notifiId) {
		DataResponse res = new DataResponse();
		try {
			notificationRepository.setFalseIsReadById(notifiId);
			Notification notifi = notificationRepository.findById(notifiId).get();
			res.setResult(notifi.getIsRead());
			res.setStatus(Constant.SUCCESS);
			res.setMessage(env.getProperty(MessageCodes.INFO_0021));
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0001));
			logger.error(e.getMessage());
		}
		return res;
	}

}
