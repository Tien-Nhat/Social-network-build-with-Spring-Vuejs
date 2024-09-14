import axios from 'axios';
import authHeader from './auth-header';

const API_URL = import.meta.env.VITE_API_URL + '/api/friend/';

class FriendService {
  addFriend(receiverId) {
    return axios.post(API_URL + 'add-friend?receiverId=' + receiverId, null, {
      headers: authHeader(),
    });
  }

  getStatus() {
    return axios.get(API_URL + 'get-map-status', {
      headers: authHeader(),
    });
  }

  getFriendRequest() {
    return axios.get(API_URL + 'get-friend-request', {
      headers: authHeader(),
    });
  }

  acceptFriend(senderId) {
    return axios.post(API_URL + 'accept-friend?senderId=' + senderId, null, {
      headers: authHeader(),
    });
  }

  getListFriend() {
    return axios.get(API_URL + 'get-list-friend', {
      headers: authHeader(),
    });
  }

  changeStatusActivity(isDisconnect, userId) {
    if (userId != null)
      return axios.post(
        API_URL +
          'change-status-friend?isDisconnect=' +
          isDisconnect +
          '&userId=' +
          userId,
        null,
        {
          headers: authHeader(),
        }
      );
    else
      return axios.post(
        API_URL + 'change-status-friend?isDisconnect=' + isDisconnect,
        null,
        {
          headers: authHeader(),
        }
      );
  }

  deleteFriend(friendId) {
    return axios.delete(API_URL + 'delete-friend?friendId=' + friendId, {
      headers: authHeader(),
    });
  }
}

export default new FriendService();
