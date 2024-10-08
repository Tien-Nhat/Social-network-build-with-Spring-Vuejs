import AuthService from '../services/auth.service';

const user = JSON.parse(localStorage.getItem('user'));
const initialState = user
  ? { status: { loggedIn: true }, user }
  : { status: { loggedIn: false }, user: null };

export const auth = {
  namespaced: true,
  state: initialState,
  actions: {
    login({ commit }, user) {
      return AuthService.login(user).then(
        (response) => {
          if (response.status == 'OK') {
            commit('loginSuccess', response.result);
            return Promise.resolve(response);
          } else {
            commit('loginFailure');
            return Promise.reject(response);
          }
        },
        (error) => {
          commit('loginFailure');
          return Promise.reject(error);
        }
      );
    },
    logingg({ commit }, oauth) {
      return AuthService.loginGoogle(oauth).then(
        (response) => {
          if (response.status == 'OK') {
            commit('loginSuccess', response.result);
            return Promise.resolve(response);
          } else {
            commit('loginFailure');
            return Promise.reject(response);
          }
        },
        (error) => {
          commit('loginFailure');
          return Promise.reject(error);
        }
      );
    },
    logout({ commit }) {
      AuthService.logout();
      commit('logout');
    },
    register({ commit }, user) {
      return AuthService.register(user).then(
        (response) => {
          commit('registerSuccess');
          return Promise.resolve(response.data);
        },
        (error) => {
          commit('registerFailure');
          return Promise.reject(error);
        }
      );
    },
  },
  mutations: {
    loginSuccess(state, user) {
      state.status.loggedIn = true;
      state.user = user;
    },
    loginFailure(state) {
      state.status.loggedIn = false;
      state.user = null;
    },
    logout(state) {
      state.status.loggedIn = false;
      state.user = null;
    },
    registerSuccess(state) {
      state.status.loggedIn = false;
    },
    registerFailure(state) {
      state.status.loggedIn = false;
    },
  },
};
