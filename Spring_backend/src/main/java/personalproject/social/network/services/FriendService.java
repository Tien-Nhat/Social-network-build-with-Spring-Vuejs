package personalproject.social.network.services;

import personalproject.social.network.response.DataResponse;

public interface FriendService {
	DataResponse addFriend(Long receiverId);

	DataResponse getMapStatus();

	DataResponse getListFriendRequest();

	DataResponse acceptRequest(Long senderId);

	DataResponse getListFriend();

	DataResponse deleteFriend(Long friendId);

	DataResponse changeStatusUser(Boolean isDisconnect, Long userId);
}
