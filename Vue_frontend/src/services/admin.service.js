import axios from 'axios';
import authHeader from './auth-header';

const API_URL = import.meta.env.VITE_API_URL + '/api/admin/';

class UserService {
  getListUser(pageNumber, pageSize) {
    return axios.get(
      API_URL + 'get-list?pageNumber=' + pageNumber + '&pageSize=' + pageSize,
      { headers: authHeader() }
    );
  }
  banUser(userId) {
    return axios.post(API_URL + 'ban?userId=' + userId, null, {
      headers: authHeader(),
    });
  }
  getListReport() {
    return axios.get(API_URL + 'get-report', {
      headers: authHeader(),
    });
  }

  searchUserBy(fieldName, txtSearch, pageNumber, pageSize) {
    return axios.get(
      API_URL +
        'search-user-by?fieldName=' +
        fieldName +
        '&txtSearch=' +
        txtSearch +
        '&pageNumber=' +
        pageNumber +
        '&pageSize=' +
        pageSize,
      { headers: authHeader() }
    );
  }

  changeStatus(reportId, status) {
    return axios.post(
      API_URL + 'change-status?reportId=' + reportId + '&status=' + status,
      null,
      {
        headers: authHeader(),
      }
    );
  }
}

export default new UserService();
