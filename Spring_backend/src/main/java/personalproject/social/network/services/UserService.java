package personalproject.social.network.services;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import personalproject.social.network.request.PasswordRequest;
import personalproject.social.network.request.UserRequest;
import personalproject.social.network.response.DataResponse;

public interface UserService {

	DataResponse getUser(Long receiverId);

	DataResponse changeAvatar(MultipartHttpServletRequest data);

	DataResponse changeBackground(MultipartHttpServletRequest data);

	DataResponse editUser(UserRequest userRequest);

	DataResponse getListUser(int pageNumber, int pageSize);

	DataResponse banUser(Long userId);

	DataResponse searchUserBy(String fieldName, String txtSearch, int pageNumber, int pageSize);

	DataResponse changePassword(PasswordRequest password);

	DataResponse getMediaUser();

	DataResponse searchSuggestUser(String name);
}
