package personalproject.social.network.services;

import personalproject.social.network.request.NotificationRequest;
import personalproject.social.network.response.DataResponse;

public interface NotificationService {

	DataResponse addNotification(NotificationRequest notificationResponse);

	DataResponse getNotification();

	DataResponse setIsRead(Long notifiId);
}
