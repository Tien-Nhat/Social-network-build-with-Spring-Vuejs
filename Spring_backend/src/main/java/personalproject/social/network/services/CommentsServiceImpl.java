package personalproject.social.network.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import personalproject.social.network.model.Comments;
import personalproject.social.network.repository.CommentsRepository;
import personalproject.social.network.repository.PostRepository;
import personalproject.social.network.repository.UserRepository;
import personalproject.social.network.request.CommentsRequest;
import personalproject.social.network.response.DataResponse;
import personalproject.social.network.util.Constant;
import personalproject.social.network.util.MessageCodes;
import personalproject.social.network.util.Utils;

@Service
public class CommentsServiceImpl implements CommentsService {

	private static final Logger logger = LoggerFactory.getLogger(CommentsService.class);

	@Autowired
	CommentsRepository commentsRepository;

	@Autowired
	PostRepository postRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	AuthenticationFacade authenticationFacade;

	@Autowired
	Utils utils;

	@Autowired
	Environment env;

	@Override
	public DataResponse addComment(CommentsRequest commentsRequest) {
		DataResponse res = new DataResponse();
		try {
			Authentication authentication = authenticationFacade.getAuthentication();
			Comments comment = new Comments();
			comment.setCommentText(commentsRequest.getCommentText());
			comment.setPost(postRepository.findById(commentsRequest.getPostId()).get());
			comment.setUser(userRepository.findByUsername(authentication.getName()).get());
			comment.setCommentDate(utils.getDateNow());
			commentsRepository.save(comment);
			res.setStatus(Constant.SUCCESS);
			res.setMessage(env.getProperty(MessageCodes.INFO_0008));
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0006));
			logger.error(e.getMessage());
		}
		return res;
	}

}
