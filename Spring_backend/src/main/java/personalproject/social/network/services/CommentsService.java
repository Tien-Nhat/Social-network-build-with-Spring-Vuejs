package personalproject.social.network.services;

import personalproject.social.network.request.CommentsRequest;
import personalproject.social.network.response.DataResponse;

public interface CommentsService {

	DataResponse addComment(CommentsRequest commentsRequest);

}
