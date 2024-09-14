package personalproject.social.network.services;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import personalproject.social.network.model.EFriendStatus;
import personalproject.social.network.model.MediaPost;
import personalproject.social.network.model.User;
import personalproject.social.network.repository.FriendRepository;
import personalproject.social.network.repository.MediaPostRepository;
import personalproject.social.network.repository.UserRepository;
import personalproject.social.network.request.PasswordRequest;
import personalproject.social.network.request.UserRequest;
import personalproject.social.network.response.DataResponse;
import personalproject.social.network.response.MediaUserResponse;
import personalproject.social.network.response.UserResponse;
import personalproject.social.network.security.jwt.JwtUtils;
import personalproject.social.network.util.Constant;
import personalproject.social.network.util.MessageCodes;
import personalproject.social.network.util.Utils;

@Service
public class UserServiceImpl implements UserService {

	private static final String URI_BAN = "/ban/messages";

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	AuthenticationFacade authenticationFacade;

	@Autowired
	FriendRepository friendRepository;

	@Value("${post.media.path}")
	String uploadDir;

	@Autowired
	Utils utils;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	Environment env;

	@Autowired
	MediaPostRepository mediapostRepository;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Override
	public DataResponse getUser(Long receiverId) {
		DataResponse res = new DataResponse();
		try {

			Optional<User> optionalUser = userRepository.findById(receiverId);
			Authentication authentication = authenticationFacade.getAuthentication();
			if (optionalUser.isPresent()) {
				User user = optionalUser.get();
				User sender = userRepository.findByUsername(authentication.getName()).get();
				String avatar = (user.getAvatar() != null) ? utils.fileToBase64StringConversion(user.getAvatar())
						: null;
				String background = (user.getBackground() != null)
						? utils.fileToBase64StringConversion(user.getBackground())
						: null;
				EFriendStatus friendStatus = friendRepository.findStatusBySenderIdAndReceiverId(sender.getId(),
						receiverId);
				UserResponse userResponse = new UserResponse(user.getFirstname(), user.getLastname(), avatar,
						user.getIntroduce(), user.getAddress(), user.getJoined(), user.getPhone(), user.getGender(),
						user.getBirthday(), user.getEmail(),
						(friendStatus == null) ? EFriendStatus.NOT_FRIEND : friendStatus, background);
				res.setStatus(Constant.SUCCESS);
				res.setMessage(env.getProperty(MessageCodes.INFO_0037));
				res.setResult(userResponse);
			} else {
				res.setStatus(Constant.ENTITY_NOT_FOUND);
				res.setMessage(env.getProperty(MessageCodes.INFO_0018));
			}

		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			logger.error(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse changePassword(PasswordRequest password) {
		DataResponse res = new DataResponse();
		try {
			Authentication authentication = authenticationFacade.getAuthentication();
			User user = userRepository.findByUsername(authentication.getName()).get();
			if (encoder.matches(password.getOldPass(), user.getPassword())) {
				user.setPassword(encoder.encode(password.getNewPass()));
				userRepository.save(user);

				res.setMessage(env.getProperty(MessageCodes.INFO_0003));
				res.setStatus(Constant.SUCCESS);
			} else {
				res.setMessage(env.getProperty(MessageCodes.ERROR_0002));
				res.setStatus(Constant.ERROR);
			}

		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0001));
			logger.error(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse changeAvatar(MultipartHttpServletRequest data) {
		DataResponse res = new DataResponse();
		try {
			String jwt = parseJwt(data);
			String username = jwtUtils.getUserNameFromJwtToken(jwt);
			MultipartFile avatar = data.getFile("avatar");
			String userId = data.getParameter("id");
			User user = userRepository.findById(Long.parseLong(userId)).get();
			if (!username.equals(user.getUsername())) {
				res.setStatus(HttpStatus.UNAUTHORIZED.name());
				res.setMessage(env.getProperty(MessageCodes.WARN_0001));
				return res;
			}
			String uniqueFileName = uploadDir + userId + "/avatar/" + System.currentTimeMillis() + "."
					+ avatar.getOriginalFilename();
			Path uploadPath = Paths.get(uniqueFileName);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			File dest = new File(uniqueFileName);
			avatar.transferTo(dest);

			user.setAvatar(uniqueFileName);
			userRepository.save(user);
			res.setStatus(Constant.SUCCESS);
			res.setMessage(env.getProperty(MessageCodes.INFO_0038));
			res.setResult(utils.fileToBase64StringConversion(uniqueFileName));
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse changeBackground(MultipartHttpServletRequest data) {
		DataResponse res = new DataResponse();
		try {
			String jwt = parseJwt(data);
			String username = jwtUtils.getUserNameFromJwtToken(jwt);
			MultipartFile background = data.getFile("background");
			String userId = data.getParameter("id");
			User user = userRepository.findById(Long.parseLong(userId)).get();
			if (!username.equals(user.getUsername())) {
				res.setStatus(HttpStatus.UNAUTHORIZED.name());
				res.setMessage(env.getProperty(MessageCodes.WARN_0001));
				return res;
			}

			String uniqueFileName = uploadDir + userId + "/background/" + System.currentTimeMillis() + "."
					+ background.getOriginalFilename();
			Path uploadPath = Paths.get(uniqueFileName);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			File dest = new File(uniqueFileName);
			background.transferTo(dest);
			user.setBackground(uniqueFileName);
			userRepository.save(user);
			res.setStatus(Constant.SUCCESS);
			res.setMessage(env.getProperty(MessageCodes.INFO_0044));
			res.setResult(utils.fileToBase64StringConversion(uniqueFileName));
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse editUser(UserRequest userRequest) {
		DataResponse res = new DataResponse();
		try {
			Optional<User> optionalUser = userRepository.findById(userRequest.getUserId());
			Authentication authentication = authenticationFacade.getAuthentication();
			if (optionalUser.isPresent()) {
				User user = optionalUser.get();
				if (authentication.getName().equals(user.getUsername()) || utils.isAdmin(authentication)) {
					user.setFirstname(userRequest.getFirstName());
					user.setLastname(userRequest.getLastName());
					user.setAddress(userRequest.getAddress());
					user.setEmail(user.getEmail());
					user.setPhone(userRequest.getPhone());
					user.setGender(userRequest.getGender());
					user.setBirthday(userRequest.getBirthday());
					user.setIntroduce(
							(userRequest.getIntroduce() != null || userRequest.getIntroduce() != Constant.EMPTY)
									? userRequest.getIntroduce()
									: user.getIntroduce());
					userRepository.save(user);
					res.setStatus(Constant.SUCCESS);
					res.setMessage(env.getProperty(MessageCodes.INFO_0039));
					res.setResult(userRequest);
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
			res.setMessage(env.getProperty(MessageCodes.ERROR_0001));
			logger.error(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse getListUser(int pageNumber, int pageSize) {
		DataResponse res = new DataResponse();
		try {
			Pageable pageable = PageRequest.of(pageNumber, pageSize);

			res.setResult(userRepository.findAllUserDetails(pageable));
			res.setStatus(Constant.SUCCESS);
			res.setMessage(env.getProperty(MessageCodes.INFO_0040));
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0001));
			logger.error(e.getMessage());
		}

		return res;
	}

	@Override
	public DataResponse banUser(Long userId) {
		DataResponse res = new DataResponse();
		try {
			userRepository.toggleBannedStatus(userId);
			Boolean banned = userRepository.isUserBannedById(userId);

			simpMessagingTemplate.convertAndSendToUser(userRepository.findUsernameById(userId), URI_BAN, banned);
			res.setStatus(Constant.SUCCESS);
			res.setMessage(
					(banned) ? env.getProperty(MessageCodes.INFO_0042) : env.getProperty(MessageCodes.INFO_0043));
			res.setResult(banned);
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0001));
			logger.error(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse searchUserBy(String fieldName, String txtSearch, int pageNumber, int pageSize) {
		DataResponse res = new DataResponse();
		try {
			Pageable pageable = PageRequest.of(pageNumber, pageSize);
			switch (fieldName) {
			case "lastname": {
				res.setResult(userRepository.searchByLastname(txtSearch, pageable));
				break;
			}
			case "firstname": {
				res.setResult(userRepository.searchByFirstname(txtSearch, pageable));
				break;
			}
			case "email": {
				res.setResult(userRepository.searchByEmail(txtSearch, pageable));
				break;
			}
			case "username": {
				res.setResult(userRepository.searchByUsername(txtSearch, pageable));
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + fieldName);
			}

			res.setStatus(Constant.SUCCESS);
			res.setMessage(env.getProperty(MessageCodes.INFO_0040));
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0001));
			logger.error(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse getMediaUser() {
		DataResponse res = new DataResponse();
		try {
			Authentication authentication = authenticationFacade.getAuthentication();
			User user = userRepository.findByUsername(authentication.getName()).get();
			List<MediaPost> listMedia = mediapostRepository.findMediaByUserId(user.getId());
			if (!listMedia.isEmpty()) {
				List<MediaUserResponse> listMediaUserReponse = new ArrayList<>();
				for (MediaPost mediaPost : listMedia) {
					MediaUserResponse mediaUserReponse = new MediaUserResponse();
					mediaUserReponse.setBase64String(utils.fileToBase64StringConversion(mediaPost.getMediaUrl()));
					mediaUserReponse.setPostId(mediaPost.getPost().getId());
					mediaUserReponse.setPostDate(mediaPost.getPost().getCreatedDatetime());
					mediaUserReponse.setContentType(mediaPost.getContentType());
					listMediaUserReponse.add(mediaUserReponse);
				}
				res.setResult(listMediaUserReponse);
			} else
				res.setResult(listMedia);
			res.setStatus(Constant.SUCCESS);
			res.setMessage(env.getProperty(MessageCodes.INFO_0041));
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0001));
			logger.error(e.getMessage());
		}
		return res;
	}

	private String parseJwt(MultipartHttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7);
		}

		return null;
	}

	@Override
	public DataResponse searchSuggestUser(String name) {
		DataResponse res = new DataResponse();
		try {
			if (name.isBlank()) {
				res.setResult(null);
				res.setStatus(Constant.SUCCESS);
				res.setMessage(env.getProperty(MessageCodes.INFO_0037));
				return res;
			}
			List<String> listSuggUser = userRepository.findFullNameContainingIgnoreCase(name);
			res.setStatus(Constant.SUCCESS);
			res.setMessage(env.getProperty(MessageCodes.INFO_0037));
			res.setResult(listSuggUser);
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0001));
			logger.error(e.getMessage());
		}
		return res;
	}

}
