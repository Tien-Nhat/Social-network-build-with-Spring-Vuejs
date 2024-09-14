package personalproject.social.network.response;

import java.util.Map;

import personalproject.social.network.model.EFriendStatus;

public class FriendStatusResponse {
	Map<Long, EFriendStatus> friendStatusSender;
	Map<Long, EFriendStatus> friendStatusReceiver;

	public Map<Long, EFriendStatus> getFriendStatusSender() {
		return friendStatusSender;
	}

	public void setFriendStatusSender(Map<Long, EFriendStatus> friendStatusSender) {
		this.friendStatusSender = friendStatusSender;
	}

	public Map<Long, EFriendStatus> getFriendStatusReceiver() {
		return friendStatusReceiver;
	}

	public void setFriendStatusReceiver(Map<Long, EFriendStatus> friendStatusReceiver) {
		this.friendStatusReceiver = friendStatusReceiver;
	}

}
