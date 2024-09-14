package personalproject.social.network.services;

import personalproject.social.network.response.DataResponse;

public interface PostLikesService {
	
	DataResponse addLike(Long postId);
	
	DataResponse getListLikes(Long postId);
}
