package personalproject.social.network.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import personalproject.social.network.model.Post;
import personalproject.social.network.model.PostLikes;
import personalproject.social.network.model.User;
import personalproject.social.network.repository.PostLikesRepository;
import personalproject.social.network.repository.PostRepository;
import personalproject.social.network.repository.UserRepository;
import personalproject.social.network.response.DataResponse;
import personalproject.social.network.util.Constant;
import personalproject.social.network.util.MessageCodes;
import personalproject.social.network.util.Utils;

@Service
public class PostLikesServiceImpl implements PostLikesService {

	private static final Logger logger = LoggerFactory.getLogger(PostLikesService.class);

	@Autowired
	PostLikesRepository postLikesReponsitory;

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
	public DataResponse addLike(Long postId) {
		DataResponse res = new DataResponse();
		try {

			Authentication authentication = authenticationFacade.getAuthentication();
			authentication.getName();

			User user = userRepository.findByUsername(authentication.getName()).get();
			Post post = postRepository.findById(postId).get();

			if (postLikesReponsitory.existsByUserIdAndPostId(user.getId(), post.getId())) {
				PostLikes postLikes = postLikesReponsitory.findByUserIdAndPostId(user.getId(), post.getId());
				postLikes.setLikeDate(utils.getDateNow());
				postLikes.setLiked(!postLikes.getLiked());
				postLikesReponsitory.save(postLikes);
				res.setStatus(Constant.SUCCESS);
				res.setMessage(postLikes.getLiked() ? env.getProperty(MessageCodes.INFO_0024)
						: env.getProperty(MessageCodes.INFO_0025));
				res.setResult(postLikes.getLiked());
			} else {
				PostLikes postLikes = new PostLikes();
				postLikes.setUser(user);
				postLikes.setPost(post);
				postLikes.setLikeDate(utils.getDateNow());
				postLikesReponsitory.save(postLikes);
				res.setStatus(Constant.SUCCESS);
				res.setMessage(env.getProperty(MessageCodes.INFO_0024));
				res.setResult(postLikes.getLiked());
			}

		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0015));
			logger.error(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse getListLikes(Long postId) {
		DataResponse res = new DataResponse();
		try {
			List<PostLikes> postLikes = postLikesReponsitory.findByUserId(postId);
			res.setResult(postLikes);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return res;
	}

}
