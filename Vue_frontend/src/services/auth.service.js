import axios from 'axios';

const API_URL = import.meta.env.VITE_API_URL + '/api/auth/';

class AuthService {
  login(user) {
    return axios
      .post(API_URL + 'signin', {
        username: user.username,
        password: user.password,
      })
      .then((response) => {
        if (response.data.result) {
          localStorage.setItem('user', JSON.stringify(response.data.result));
        }
        return response.data;
      });
  }

  logout() {
    localStorage.removeItem('user');
  }

  register(user) {
    return axios.post(API_URL + 'signup', {
      username: user.username,
      email: user.email,
      password: user.password,
      firstname: user.firstname,
      lastname: user.lastname,
      birthday: user.birthday,
      phone: user.phone,
      gender: user.gender,
      address: user.address,
    });
  }

  loginGoogle(oauth) {
    return axios
      .post(
        API_URL + 'oauth2/google?code=' + oauth.code + '&type=' + oauth.type,
        {}
      )
      .then((response) => {
        if (response.data.result) {
          localStorage.setItem('user', JSON.stringify(response.data.result));
        }
        return response.data;
      });
  }
}

export default new AuthService();
