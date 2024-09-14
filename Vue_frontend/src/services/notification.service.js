import axios from 'axios';
import authHeader from './auth-header';

const API_URL = import.meta.env.VITE_API_URL + '/api/notifi/';

class NotifiService {
  addNotifi(notifiRes) {
    return axios.post(API_URL + 'add', notifiRes, {
      headers: authHeader(),
    });
  }

  getNotifi() {
    return axios.get(API_URL + 'get', { headers: authHeader() });
  }

  setIsReadNotifi(notifiId) {
    return axios.post(API_URL + 'set-is-read?notifiId=' + notifiId, null, {
      headers: authHeader(),
    });
  }
}

export default new NotifiService();
