import axios from 'axios';
import authHeader from './auth-header';

const API_URL = import.meta.env.VITE_API_URL + '/api/post/';

class PostService {
  addPost(formData) {
    return axios.post(API_URL + 'add', formData, {
      headers: authHeader(),
    });
  }

  getPosts(id = null) {
    if (id != null)
      return axios.get(API_URL + 'posts?userId=' + id, {
        headers: authHeader(),
      });
    else return axios.get(API_URL + 'posts', { headers: authHeader() });
  }

  getPost(postId) {
    return axios.get(API_URL + 'get-post?postId=' + postId, {
      headers: authHeader(),
    });
  }

  deletePost(postId) {
    return axios.delete(API_URL + 'delete?postId=' + postId, {
      headers: authHeader(),
    });
  }

  restorePost(postId) {
    return axios.put(API_URL + 'restore?postId=' + postId, null, {
      headers: authHeader(),
    });
  }

  editPost(formData) {
    return axios.put(API_URL + 'edit', formData, {
      headers: authHeader(),
    });
  }

  searchPostUser(txtSearch) {
    return axios.get(API_URL + 'search?txtSearch=' + txtSearch, {
      headers: authHeader(),
    });
  }

  likePost(postId) {
    return axios.post(API_URL + 'add-like?postId=' + postId, null, {
      headers: authHeader(),
    });
  }

  commentPost(comment) {
    return axios.post(API_URL + 'add-comment', comment, {
      headers: authHeader(),
    });
  }

  getTypeReport() {
    return axios.get(API_URL + 'report-type', {
      headers: authHeader(),
    });
  }
}

export default new PostService();
