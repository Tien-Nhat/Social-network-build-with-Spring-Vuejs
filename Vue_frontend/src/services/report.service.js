import axios from 'axios';
import authHeader from './auth-header';

const API_URL = import.meta.env.VITE_API_URL + '/api/report/';

class ReportService {
  getTypeReport() {
    return axios.get(API_URL + 'report-type', {
      headers: authHeader(),
    });
  }

  reportPost(report) {
    return axios.post(API_URL + 'add-report', report, {
      headers: authHeader(),
    });
  }
}

export default new ReportService();
