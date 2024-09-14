package personalproject.social.network.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import personalproject.social.network.model.EFriendStatus;
import personalproject.social.network.model.EUserStatus;
import personalproject.social.network.model.Friends;
import personalproject.social.network.model.User;
import personalproject.social.network.repository.FriendRepository;
import personalproject.social.network.repository.UserRepository;
import personalproject.social.network.response.DataResponse;
import personalproject.social.network.response.FriendListResponse;
import personalproject.social.network.response.FriendRequestResponse;
import personalproject.social.network.response.FriendStatusResponse;
import personalproject.social.network.response.ListFriendRequestResponse;
import personalproject.social.network.util.Constant;
import personalproject.social.network.util.MessageCodes;
import personalproject.social.network.util.Utils;

@Service
public class FriendServiceImpl implements FriendService {

	private static final String URI_FRIEND = "/friend/messages";
	private static final String URI_UPDATE = "/update/messages";
	private static final String SEND_FRIEND_REQUEST = " has sent you a friend request.";
	private static final Logger logger = LoggerFactory.getLogger(FriendService.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	FriendRepository friendRepository;

	@Autowired
	AuthenticationFacade authenticationFacade;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	Utils utils;

	@Autowired
	Environment env;

	@Override
	public DataResponse addFriend(Long receiverId) {
		DataResponse res = new DataResponse();
		try {
			Authentication authentication = authenticationFacade.getAuthentication();
			User sender = userRepository.findByUsername(authentication.getName()).get();
			Optional<Friends> friendOptional = friendRepository.findBySenderIdAndReceiverId(sender.getId(), receiverId);
			String content = Constant.EMPTY;
			EFriendStatus status = EFriendStatus.NOT_FRIEND;
			if (friendOptional.isPresent()) {
				User receiver = userRepository.findById(receiverId).get();
				Friends friend = friendOptional.get();
				status = friend.getStatus().equals(EFriendStatus.PENDING) ? EFriendStatus.NOT_FRIEND
						: EFriendStatus.PENDING;
				friend.setStatus(status);
				friend.setSender(sender);
				friend.setReceiver(receiver);
				friend.setRequestDate(
						status.equals(EFriendStatus.PENDING) ? utils.getDateNow() : friend.getRequestDate());
				friendRepository.save(friend);
				res.setResult(friend.getStatus());
				res.setMessage(env.getProperty(MessageCodes.INFO_0009));
				content = status.equals(EFriendStatus.PENDING) ? env.getProperty(MessageCodes.INFO_0011)
						: env.getProperty(MessageCodes.INFO_0012);
			} else {
				Friends friend = new Friends();
				friend.setReceiver(userRepository.findById(receiverId).get());
				friend.setSender(sender);
				friend.setRequestDate(utils.getDateNow());
				friendRepository.save(friend);
				res.setResult(friend.getStatus());
				res.setMessage(env.getProperty(MessageCodes.INFO_0010));
				content = env.getProperty(MessageCodes.INFO_0013);
				status = friend.getStatus();
			}

			User receiver = userRepository.findById(receiverId).get();
			FriendRequestResponse friendRequestRespone = new FriendRequestResponse();
			friendRequestRespone.setAvatar(
					(sender.getAvatar() != null) ? utils.fileToBase64StringConversion(sender.getAvatar()) : null);
			friendRequestRespone.setFirstName(sender.getFirstname());
			friendRequestRespone.setLastName(sender.getLastname());
			friendRequestRespone.setContent(content);
			friendRequestRespone.setStatus(status);
			friendRequestRespone.setSenderId(sender.getId());
			simpMessagingTemplate.convertAndSendToUser(receiver.getUsername(), URI_FRIEND, friendRequestRespone);

			res.setStatus(Constant.SUCCESS);

		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0007));
			logger.error(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse getMapStatus() {
		DataResponse res = new DataResponse();
		try {
			Authentication authentication = authenticationFacade.getAuthentication();
			User sender = userRepository.findByUsername(authentication.getName()).get();
			Map<Long, EFriendStatus> FrStatusSenderId = friendRepository.findFriendStatusBySenderId(sender.getId());
			Map<Long, EFriendStatus> FrStatusReceiverId = friendRepository.findFriendStatusByReceiverId(sender.getId());
			FriendStatusResponse friendStatusReponse = new FriendStatusResponse();
			friendStatusReponse.setFriendStatusSender(FrStatusSenderId);
			friendStatusReponse.setFriendStatusReceiver(FrStatusReceiverId);
			res.setResult(friendStatusReponse);
			res.setStatus(Constant.SUCCESS);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0008));
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0009));
			logger.error(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse getListFriendRequest() {
		DataResponse res = new DataResponse();
		try {
			Authentication authentication = authenticationFacade.getAuthentication();
			User receiver = userRepository.findByUsername(authentication.getName()).get();
			List<User> listReFriend = friendRepository.findPendingFriendsByReceiverId(receiver.getId());
			List<FriendRequestResponse> listRequestReponse = new ArrayList<>();

			for (User sender : listReFriend) {
				FriendRequestResponse friendRequestRespone = new FriendRequestResponse();
				friendRequestRespone.setAvatar(
						(sender.getAvatar() != null) ? utils.fileToBase64StringConversion(sender.getAvatar()) : null);
				friendRequestRespone.setFirstName(sender.getFirstname());
				friendRequestRespone.setLastName(sender.getLastname());
				friendRequestRespone.setContent(SEND_FRIEND_REQUEST);
				friendRequestRespone.setStatus(EFriendStatus.PENDING);
				friendRequestRespone.setSenderId(sender.getId());

				listRequestReponse.add(friendRequestRespone);
			}
			ListFriendRequestResponse listRe = new ListFriendRequestResponse();
			Long countRequestPending = friendRepository.countPendingFriendRequestsByReceiverId(receiver.getId());
			listRe.setListFriendRe(listRequestReponse);
			listRe.setCountRequestPending(countRequestPending);

			res.setResult(listRe);
			res.setStatus(Constant.SUCCESS);
			res.setMessage(env.getProperty(MessageCodes.INFO_0014));
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0010));
			logger.error(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse acceptRequest(Long senderId) {
		DataResponse res = new DataResponse();
		try {
			Authentication authentication = authenticationFacade.getAuthentication();
			User receiver = userRepository.findByUsername(authentication.getName()).get();
			User sender = userRepository.findById(senderId).get();
			Friends friend = friendRepository.findBySenderIdAndReceiverId(senderId, receiver.getId()).get();
			friend.setStatus(EFriendStatus.ACCEPTED);
			friendRepository.save(friend);

			FriendRequestResponse friendRequestRespone = new FriendRequestResponse();
			friendRequestRespone.setAvatar(
					(receiver.getAvatar() != null) ? utils.fileToBase64StringConversion(receiver.getAvatar()) : null);
			friendRequestRespone.setFirstName(receiver.getFirstname());
			friendRequestRespone.setLastName(receiver.getLastname());
			friendRequestRespone.setContent(" is now your friend.");
			friendRequestRespone.setStatus(EFriendStatus.ACCEPTED);
			friendRequestRespone.setSenderId(receiver.getId());
			simpMessagingTemplate.convertAndSendToUser(sender.getUsername(), URI_FRIEND, friendRequestRespone);

			res.setResult(friend.getStatus());
			res.setStatus(Constant.SUCCESS);
			res.setMessage(env.getProperty(MessageCodes.INFO_0015));
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0011));
			logger.error(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse getListFriend() {
		DataResponse res = new DataResponse();
		try {
			Authentication authentication = authenticationFacade.getAuthentication();
			User user = userRepository.findByUsername(authentication.getName()).get();
			List<Long> listFriendId = friendRepository.findFriendIdsByUserIdAndStatusAccept(user.getId());
			List<FriendListResponse> listFriendRe = new ArrayList<>();
			for (Long friendId : listFriendId) {
				User friend = userRepository.findById(friendId).get();
				FriendListResponse friendRe = new FriendListResponse();
				friendRe.setUserId(friend.getId());
				friendRe.setAvatar(
						(friend.getAvatar() != null) ? utils.fileToBase64StringConversion(friend.getAvatar()) : null);
				friendRe.setFirstName(friend.getFirstname());
				friendRe.setLastName(friend.getLastname());
				friendRe.setStatus(friend.getStatus());
				listFriendRe.add(friendRe);
			}
			res.setResult(listFriendRe);
			res.setStatus(Constant.SUCCESS);
			res.setMessage(env.getProperty(MessageCodes.INFO_0016));
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0012));
			logger.error(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse changeStatusUser(Boolean isDisconnect, Long userId) {
		DataResponse res = new DataResponse();
		try {
			User user = userRepository.findById(userId).get();
			user.setStatus((isDisconnect) ? EUserStatus.OFFLINE : EUserStatus.ONLINE);
			userRepository.save(user);
			FriendListResponse friendRe = new FriendListResponse();
			friendRe.setUserId(user.getId());
			friendRe.setStatus(user.getStatus());
			simpMessagingTemplate.convertAndSend(URI_UPDATE, friendRe);
			res.setResult(user.getStatus());
			res.setStatus(Constant.SUCCESS);
			res.setMessage(env.getProperty(MessageCodes.INFO_0017));
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0013));
//			logger.error(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse deleteFriend(Long friendId) {
		DataResponse res = new DataResponse();
		try {
			Authentication authentication = authenticationFacade.getAuthentication();
			User user = userRepository.findByUsername(authentication.getName()).get();
			Long friendShipId = friendRepository.findFriendIdsBySenderAndReceiver(user.getId(), friendId);
			Friends friend = friendRepository.findById(friendShipId).get();
			friend.setStatus(EFriendStatus.NOT_FRIEND);
			friendRepository.save(friend);

			res.setResult(EFriendStatus.NOT_FRIEND);
			res.setStatus(Constant.SUCCESS);
			res.setMessage(env.getProperty(MessageCodes.INFO_0018));
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0014));
			logger.error(e.getMessage());
		}
		return res;
	}

}
