import axios from 'axios';
import authHeader from './auth-header';

const API_URL = import.meta.env.VITE_API_URL + '/api/user/';

class UserService {
  getUser(id) {
    return axios.get(API_URL + 'get?id=' + id, { headers: authHeader() });
  }

  changeAvatar(formData) {
    return axios.post(API_URL + 'change-avatar', formData, {
      headers: authHeader(),
    });
  }

  changeBackground(formData) {
    return axios.post(API_URL + 'change-background', formData, {
      headers: authHeader(),
    });
  }

  editUser(infoUser) {
    return axios.post(API_URL + 'edit', infoUser, {
      headers: authHeader(),
    });
  }

  changePass(passwordRequest) {
    return axios.post(API_URL + 'change-password', passwordRequest, {
      headers: authHeader(),
    });
  }

  getPhoto() {
    return axios.get(API_URL + 'get-photo', {
      headers: authHeader(),
    });
  }

  searchSuggestUser(txtSearch) {
    return axios.get(API_URL + 'search-suggest-user?txtSearch=' + txtSearch, {
      headers: authHeader(),
    });
  }
}

export default new UserService();
