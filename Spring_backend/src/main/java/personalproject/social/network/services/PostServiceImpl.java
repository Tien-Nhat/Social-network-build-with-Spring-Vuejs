package personalproject.social.network.services;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import personalproject.social.network.model.Comments;
import personalproject.social.network.model.EFriendStatus;
import personalproject.social.network.model.MediaPost;
import personalproject.social.network.model.Post;
import personalproject.social.network.model.User;
import personalproject.social.network.repository.CommentsRepository;
import personalproject.social.network.repository.FriendRepository;
import personalproject.social.network.repository.MediaPostRepository;
import personalproject.social.network.repository.PostLikesRepository;
import personalproject.social.network.repository.PostRepository;
import personalproject.social.network.repository.UserRepository;
import personalproject.social.network.request.PostRequest;
import personalproject.social.network.response.CommentsResponse;
import personalproject.social.network.response.DataResponse;
import personalproject.social.network.response.MediaResponse;
import personalproject.social.network.response.PostResponse;
import personalproject.social.network.response.PostSharedResponse;
import personalproject.social.network.response.SearchResponse;
import personalproject.social.network.response.UserLiteResponse;
import personalproject.social.network.util.Constant;
import personalproject.social.network.util.MessageCodes;
import personalproject.social.network.util.Utils;

@Service
public class PostServiceImpl implements PostService {

	private static final Logger logger = LoggerFactory.getLogger(PostService.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	Utils utils;

	@Autowired
	PostRepository postRepository;

	@Autowired
	FriendRepository friendRepository;

	@Autowired
	PostLikesRepository postLikesRepository;

	@Autowired
	CommentsRepository commentsRepository;

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	MediaPostRepository mediapostRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	AuthenticationFacade authenticationFacade;

	@Value("${post.media.path}")
	String uploadDir;

	@Autowired
	Environment env;

	@Override
	public DataResponse addPost(MultipartHttpServletRequest data) {
		DataResponse res = new DataResponse();
		try {
			List<MultipartFile> medias = data.getFiles("media");
			String postString = data.getParameter("post");
			PostRequest postRe = objectMapper.readValue(postString, PostRequest.class);
			Optional<User> optionalUser = userRepository.findById(postRe.getUserId());

			if (optionalUser.isPresent()) {
				User user = optionalUser.get();
				String dateNow = utils.getDateNow();

				Post post = new Post(postRe.getContent(), null, dateNow);
				post.setUser(user);
				post.setSharedId(postRe.getSharedId());
				Post postNewSave = postRepository.save(post);

				String mediaUrl = uploadDir + postRe.getUserId() + "/" + postNewSave.getId() + "/";
				if (medias != null) {
					Path uploadPath = Paths.get(mediaUrl);
					if (!Files.exists(uploadPath)) {
						Files.createDirectories(uploadPath);
					}
					List<MediaPost> mediaPostList = new ArrayList<>();
					for (MultipartFile media : medias) {
						String fileName = mediaUrl + System.currentTimeMillis() + "." + media.getOriginalFilename();
						File dest = new File(fileName);
						media.transferTo(dest);
						MediaPost mediaPost = new MediaPost(fileName, null,
								URLConnection.guessContentTypeFromName(fileName), dateNow, postNewSave);
						mediaPostList.add(mediaPost);
					}
					postNewSave.setMediaPost(mediaPostList);
				}
				Post postNew = postRepository.save(postNewSave);

				PostResponse postResponse = new PostResponse();
				postResponse.setContent(postNew.getContent());
				postResponse.setMedias(getMedias(mediapostRepository.findByPost_IdAndDeletedFalse(postNew.getId())));
				postResponse.setUserId(postNew.getUser().getId());
				postResponse.setIntroduce(postNew.getUser().getIntroduce());
				postResponse.setFirstName(postNew.getUser().getFirstname());
				postResponse.setLastName(postNew.getUser().getLastname());
				postResponse.setCreatedDatetime(postNew.getCreatedDatetime());
				postResponse.setAvatar((postNew.getUser().getAvatar() != null)
						? utils.fileToBase64StringConversion(postNew.getUser().getAvatar())
						: null);
				postResponse.setId(postNew.getId());
				postResponse.setLiked(false);
				postResponse.setListLiked(
						getListUserLiked(postLikesRepository.findUsersLikedPost(postNew.getId(), user.getId())));
				postResponse.setListComment(getListComment(commentsRepository.findByPostId(postNew.getId())));
				postResponse.setPostShare(getPostShared(postNew.getSharedId()));

				res.setResult(postResponse);
				res.setStatus(Constant.SUCCESS);
				res.setMessage(env.getProperty(MessageCodes.INFO_0001));
			} else {
				res.setStatus(Constant.ENTITY_NOT_FOUND);
				res.setMessage(Constant.USER_NOT_FOUND);
			}

		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(e.getMessage());
		}

		return res;
	}

	@Override
	public DataResponse getPosts(Long userId) {
		DataResponse res = new DataResponse();
		try {
			List<Post> posts = (userId == null) ? postRepository.findAllNotDeleted()
					: postRepository.findByUser_IdAndDeletedFalse(userId);

			if (!posts.isEmpty()) {
				List<PostResponse> postResponses = new ArrayList<>();
				Authentication authentication = authenticationFacade.getAuthentication();
				User user = userRepository.findByUsername(authentication.getName()).get();
				Map<Long, Boolean> mapLikes = postLikesRepository.findLikedByUserId(user.getId());
				Map<Long, EFriendStatus> FrStatus = friendRepository.findFriendStatusBySenderId(user.getId());
				for (Post post : posts) {

					PostResponse postResponse = new PostResponse();
					postResponse.setContent(post.getContent());
					postResponse.setMedias(getMedias(mediapostRepository.findByPost_IdAndDeletedFalse(post.getId())));
					postResponse.setUserId(post.getUser().getId());
					postResponse.setIntroduce(post.getUser().getIntroduce());
					postResponse
							.setFriendStatus((FrStatus.get(post.getUser().getId()) == null) ? EFriendStatus.NOT_FRIEND
									: FrStatus.get(post.getUser().getId()));
					postResponse.setFirstName(post.getUser().getFirstname());
					postResponse.setLastName(post.getUser().getLastname());
					postResponse.setCreatedDatetime(post.getCreatedDatetime());
					postResponse.setAvatar((post.getUser().getAvatar() != null)
							? utils.fileToBase64StringConversion(post.getUser().getAvatar())
							: null);
					postResponse.setId(post.getId());
					postResponse.setLiked((mapLikes.get(post.getId()) == null) ? false : mapLikes.get(post.getId()));
					postResponse.setListLiked(
							getListUserLiked(postLikesRepository.findUsersLikedPost(post.getId(), user.getId())));
					postResponse.setListComment(getListComment(commentsRepository.findByPostId(post.getId())));
					postResponse.setPostShare(getPostShared(post.getSharedId()));
					postResponses.add(postResponse);
				}
				res.setStatus(Constant.SUCCESS);
				res.setMessage(env.getProperty(MessageCodes.INFO_0026));
				res.setResult(postResponses);
			} else {
				res.setStatus(Constant.ENTITY_NOT_FOUND);
				res.setMessage(env.getProperty(MessageCodes.ERROR_0016));
			}
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(e.getMessage());
			logger.error(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse deletePost(Long postId) {
		DataResponse res = new DataResponse();
		try {
			Authentication authentication = authenticationFacade.getAuthentication();
			String userName = postRepository.findById(postId).get().getUser().getUsername();
			if (authentication.getName().equals(userName) || utils.isAdmin(authentication)) {
				postRepository.deletePostById(postId);
				res.setStatus(Constant.SUCCESS);
				res.setMessage(env.getProperty(MessageCodes.INFO_0027));
			} else {
				res.setStatus(HttpStatus.UNAUTHORIZED.name());
				res.setMessage(Constant.YOUR_ACCOUNT_DO_NOT_HAVE_PERMISSION);
			}

		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0001));
			logger.error(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse restorePost(Long postId) {
		DataResponse res = new DataResponse();
		try {
			Authentication authentication = authenticationFacade.getAuthentication();
			String userName = postRepository.findById(postId).get().getUser().getUsername();
			if (authentication.getName().equals(userName) || utils.isAdmin(authentication)) {
				postRepository.undeletePostById(postId);
				res.setStatus(Constant.SUCCESS);
				res.setMessage(env.getProperty(MessageCodes.INFO_0028));
			} else {
				res.setStatus(HttpStatus.UNAUTHORIZED.name());
				res.setMessage(Constant.YOUR_ACCOUNT_DO_NOT_HAVE_PERMISSION);
			}

		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse editPost(MultipartHttpServletRequest data) {
		DataResponse res = new DataResponse();
		try {
			List<MultipartFile> medias = data.getFiles("media");
			String postString = data.getParameter("post");
			PostRequest postRe = objectMapper.readValue(postString, PostRequest.class);
			Optional<User> optionalUser = userRepository.findById(postRe.getUserId());
			if (optionalUser.isPresent()) {
				Authentication authentication = authenticationFacade.getAuthentication();
				String userName = optionalUser.get().getUsername();
				if (authentication.getName().equals(userName) || utils.isAdmin(authentication)) {
					Post postOld = postRepository.findById(postRe.getPostId()).get();
					if (!medias.isEmpty()) {

						String mediaUrl = uploadDir + postRe.getUserId() + "/" + postRe.getPostId() + "/";
						Path uploadPath = Paths.get(mediaUrl);
						if (!Files.exists(uploadPath)) {
							Files.createDirectories(uploadPath);
						}

						for (MultipartFile media : medias) {
							MediaPost mediaPost = new MediaPost();
							String fileName = mediaUrl + System.currentTimeMillis() + "." + media.getOriginalFilename();
							File dest = new File(fileName);
							media.transferTo(dest);
							mediaPost.setCreatedDatetime(utils.getDateNow());
							mediaPost.setMediaUrl(fileName);
							mediaPost.setContentType(URLConnection.guessContentTypeFromName(fileName));
							mediaPost.setPost(postOld);
							mediapostRepository.save(mediaPost);
						}
					}
					postRepository.updatePostContentAndMedia(postRe.getPostId(), postRe.getContent(),
							postOld.getCreatedDatetime(), postRe.getSharedId());
					mediapostRepository.deleteByListId(postRe.getIdDeleted());
					res.setStatus(Constant.SUCCESS);
					res.setMessage(env.getProperty(MessageCodes.INFO_0029));
				} else {
					res.setStatus(HttpStatus.UNAUTHORIZED.name());
					res.setMessage(Constant.YOUR_ACCOUNT_DO_NOT_HAVE_PERMISSION);
				}
			} else {
				res.setStatus(Constant.ENTITY_NOT_FOUND);
				res.setMessage(Constant.USER_NOT_FOUND);
			}

		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0017));
			logger.error(e.getMessage());
		}

		return res;
	}

	@Override
	public DataResponse searchPostUser(String textSearch) {
		DataResponse res = new DataResponse();
		try {
			res.setStatus(Constant.ENTITY_NOT_FOUND);
			res.setMessage(env.getProperty(MessageCodes.INFO_0030));
			SearchResponse searchResponse = new SearchResponse();
			List<Post> listPost = postRepository.findPostsByContentOrUserNameContaining(textSearch);
			List<User> listUser = userRepository.findUsersBySearchCriteria(textSearch);
			Authentication authentication = authenticationFacade.getAuthentication();
			User userRequest = userRepository.findByUsername(authentication.getName()).get();
			Map<Long, Boolean> mapLikes = postLikesRepository.findLikedByUserId(userRequest.getId());

			if (!listPost.isEmpty()) {
				List<PostResponse> listPostResponse = new ArrayList<>();

				for (Post post : listPost) {

					PostResponse postResponse = new PostResponse();
					postResponse.setContent(post.getContent());
					postResponse.setMedias(getMedias(mediapostRepository.findByPost_IdAndDeletedFalse(post.getId())));
					postResponse.setUserId(post.getUser().getId());
					postResponse.setFirstName(post.getUser().getFirstname());
					postResponse.setLastName(post.getUser().getLastname());
					postResponse.setCreatedDatetime(post.getCreatedDatetime());
					postResponse.setAvatar((post.getUser().getAvatar() != null)
							? utils.fileToBase64StringConversion(post.getUser().getAvatar())
							: null);
					postResponse.setId(post.getId());
					postResponse.setLiked((mapLikes.get(post.getId()) == null) ? false : mapLikes.get(post.getId()));
					postResponse.setListLiked(getListUserLiked(
							postLikesRepository.findUsersLikedPost(post.getId(), userRequest.getId())));
					postResponse.setListComment(getListComment(commentsRepository.findByPostId(post.getId())));
					postResponse.setPostShare(getPostShared(post.getSharedId()));
					listPostResponse.add(postResponse);
				}
				searchResponse.setListPost(listPostResponse);
				res.setStatus(Constant.SUCCESS);
				res.setMessage(env.getProperty(MessageCodes.INFO_0031));
			}

			if (!listUser.isEmpty()) {
				List<UserLiteResponse> listUserSearchReponse = new ArrayList<>();

				for (User user : listUser) {
					UserLiteResponse userSearchReponse = new UserLiteResponse();
					userSearchReponse.setUserId(user.getId());
					userSearchReponse.setAddress(user.getAddress());
					userSearchReponse.setAvatar(
							(user.getAvatar() != null) ? utils.fileToBase64StringConversion(user.getAvatar()) : null);
					userSearchReponse.setFirstName(user.getFirstname());
					userSearchReponse.setLastName(user.getLastname());
					listUserSearchReponse.add(userSearchReponse);
				}
				searchResponse.setListUser(listUserSearchReponse);
				res.setStatus(Constant.SUCCESS);
				res.setMessage(env.getProperty(MessageCodes.INFO_0031));
			}

			res.setResult(searchResponse);

		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0001));
			logger.error(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse getPost(Long postId) {
		DataResponse res = new DataResponse();
		try {
			Authentication authentication = authenticationFacade.getAuthentication();
			Long userLikedId = userRepository.findByUsername(authentication.getName()).get().getId();
			Post post = postRepository.findById(postId).get();
			if (!post.getDeleted() || utils.isAdmin(authentication)) {
				Boolean liked = postLikesRepository.findLikedByPostIdAndUserId(post.getId(), userLikedId);

				PostResponse postResponse = new PostResponse();
				postResponse.setAvatar((post.getUser().getAvatar() != null)
						? utils.fileToBase64StringConversion(post.getUser().getAvatar())
						: null);
				postResponse.setContent(post.getContent());
				postResponse.setCreatedDatetime(post.getCreatedDatetime());
				postResponse.setFirstName(post.getUser().getFirstname());
				postResponse.setLastName(post.getUser().getLastname());
				postResponse.setLiked((liked == null) ? false : liked);
				postResponse.setListComment(getListComment(commentsRepository.findByPostId(post.getId())));
				postResponse.setListLiked(
						getListUserLiked(postLikesRepository.findUsersLikedPost(post.getId(), userLikedId)));
				postResponse.setMedias(getMedias(mediapostRepository.findByPost_IdAndDeletedFalse(post.getId())));
				postResponse.setPostShare(getPostShared(post.getSharedId()));
				postResponse.setUserId(post.getUser().getId());
				postResponse.setId(postId);

				res.setStatus(Constant.SUCCESS);
				res.setMessage(env.getProperty(MessageCodes.INFO_0032));
				res.setResult(postResponse);
			} else {
				res.setStatus(Constant.SUCCESS);
				res.setMessage(env.getProperty(MessageCodes.INFO_0033));
			}
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0001));
			logger.error(e.getMessage());
		}
		return res;
	}

	public List<MediaResponse> getMedias(List<MediaPost> mediaPost) throws IOException {
		List<MediaResponse> mediaResponse = new ArrayList<>();
		for (MediaPost mediaItem : mediaPost) {
			MediaResponse media = new MediaResponse(mediaItem.getId(), mediaItem.getDescription(),
					utils.fileToBase64StringConversion(mediaItem.getMediaUrl()), mediaItem.getContentType());
			mediaResponse.add(media);
		}
		return mediaResponse;
	}

	public List<UserLiteResponse> getListUserLiked(List<User> listUser) throws IOException {
		List<UserLiteResponse> listUserLiked = new ArrayList<>();
		for (User user : listUser) {
			String avatar = (user.getAvatar() != null) ? utils.fileToBase64StringConversion(user.getAvatar()) : null;
			UserLiteResponse media = new UserLiteResponse(user.getId(), avatar, user.getFirstname(),
					user.getLastname());
			listUserLiked.add(media);
		}
		return listUserLiked;
	}

	public List<CommentsResponse> getListComment(List<Comments> listComment) throws IOException {
		List<CommentsResponse> listCommentResponse = new ArrayList<>();
		for (Comments commentItem : listComment) {
			String avatar = (commentItem.getUser().getAvatar() != null)
					? utils.fileToBase64StringConversion(commentItem.getUser().getAvatar())
					: null;
			User user = commentItem.getUser();
			CommentsResponse comment = new CommentsResponse(user.getId(), avatar, commentItem.getCommentDate(),
					commentItem.getCommentText(), user.getFirstname(), user.getLastname(), user.getIntroduce());
			listCommentResponse.add(comment);
		}
		return listCommentResponse;
	}

	public PostSharedResponse getPostShared(Long postId) throws IOException {
		if (postId != null) {
			PostSharedResponse postSharedResponse = new PostSharedResponse();
			Post post = postRepository.findById(postId).get();
			if (!post.getDeleted()) {
				User user = post.getUser();
				postSharedResponse.setMedias(getMedias(mediapostRepository.findByPost_IdAndDeletedFalse(post.getId())));
				postSharedResponse.setContent(post.getContent());
				postSharedResponse.setAvatar(
						(user.getAvatar() != null) ? utils.fileToBase64StringConversion(user.getAvatar()) : null);
				postSharedResponse.setCreatedDatetime(post.getCreatedDatetime());
				postSharedResponse.setFirstName(user.getFirstname());
				postSharedResponse.setLastName(user.getLastname());
				postSharedResponse.setId(post.getId());
				postSharedResponse.setUserId(user.getId());
				postSharedResponse.setIntroduce(user.getIntroduce());

				return postSharedResponse;
			}
			return null;
		}
		return null;
	}

}
