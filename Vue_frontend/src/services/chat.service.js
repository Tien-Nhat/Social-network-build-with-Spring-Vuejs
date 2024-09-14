import axios from 'axios';
import authHeader from './auth-header';

const API_URL = import.meta.env.VITE_API_URL + '/api/chat/';

class ChatService {
  sendMessage(formData) {
    return axios.post(API_URL + 'send', formData, {
      headers: authHeader(),
    });
  }
  getHistory(receiverId) {
    return axios.get(API_URL + 'get-history?receiverId=' + receiverId, {
      headers: authHeader(),
    });
  }
}

export default new ChatService();
