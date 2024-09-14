package personalproject.social.network.response;

import java.util.List;

public class NotificationResponse {

	private Long countNotRead;
	private List<NotificationLiteResponse> listNotifi;

	public Long getCountNotRead() {
		return countNotRead;
	}

	public void setCountNotRead(Long countNotRead) {
		this.countNotRead = countNotRead;
	}

	public List<NotificationLiteResponse> getListNotifi() {
		return listNotifi;
	}

	public void setListNotifi(List<NotificationLiteResponse> listNotifi) {
		this.listNotifi = listNotifi;
	}

}
