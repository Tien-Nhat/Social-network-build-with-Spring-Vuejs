package personalproject.social.network.services;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import personalproject.social.network.response.DataResponse;

public interface PostService {

	DataResponse addPost(MultipartHttpServletRequest data);

	DataResponse getPosts(Long id);

	DataResponse deletePost(Long postId);

	DataResponse restorePost(Long postId);

	DataResponse editPost(MultipartHttpServletRequest data);

	DataResponse searchPostUser(String textSearch);

	DataResponse getPost(Long postId);

}
