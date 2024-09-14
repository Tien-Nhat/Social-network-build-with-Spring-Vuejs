package personalproject.social.network.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import personalproject.social.network.model.EFriendStatus;
import personalproject.social.network.model.Friends;
import personalproject.social.network.model.User;

public interface FriendRepository extends JpaRepository<Friends, Long> {
	@Query("SELECT f.status FROM Friends f WHERE (f.sender.id = :senderId AND f.receiver.id = :receiverId) OR (f.sender.id = :receiverId AND f.receiver.id = :senderId)")
	EFriendStatus findStatusBySenderIdAndReceiverId(Long senderId, Long receiverId);

	@Query("SELECT f.receiver.id,f.status FROM Friends f WHERE f.sender.id = :senderId")
	List<Object[]> findStatusBySenderId(Long senderId);

	default Map<Long, EFriendStatus> findFriendStatusBySenderId(Long senderId) {
		return findStatusBySenderId(senderId).stream().collect(Collectors.toMap(obj -> (Long) obj[0], // receiverId
				obj -> (EFriendStatus) obj[1]// status
		));
	}

	@Query("SELECT f.sender.id,f.status FROM Friends f WHERE f.receiver.id = :receiverId")
	List<Object[]> findStatusByReceiverId(Long receiverId);

	default Map<Long, EFriendStatus> findFriendStatusByReceiverId(Long receiverId) {
		return findStatusByReceiverId(receiverId).stream().collect(Collectors.toMap(obj -> (Long) obj[0], // senderId
				obj -> (EFriendStatus) obj[1]// status
		));
	}

	@Query("SELECT f FROM Friends f WHERE (f.sender.id = :senderId AND f.receiver.id = :receiverId) OR (f.sender.id = :receiverId AND f.receiver.id = :senderId)")
	Optional<Friends> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

	@Query("SELECT f.sender FROM Friends f WHERE f.receiver.id = :receiverId AND f.status = 'PENDING' ORDER BY f.requestDate DESC")
	List<User> findPendingFriendsByReceiverId(Long receiverId);

	@Query("SELECT COUNT(f) FROM Friends f WHERE f.receiver.id = :receiverId AND f.status = 'PENDING'")
	Long countPendingFriendRequestsByReceiverId(Long receiverId);

	@Query("SELECT CASE WHEN f.receiver.id = :userId THEN f.sender.id ELSE f.receiver.id END FROM Friends f"
			+ " WHERE (f.receiver.id = :userId OR f.sender.id = :userId) AND f.status = 'ACCEPTED'")
	List<Long> findFriendIdsByUserIdAndStatusAccept(Long userId);

	@Query("SELECT f.id FROM Friends f WHERE (f.sender.id = :senderId AND f.receiver.id = :receiverId) OR (f.sender.id = :receiverId AND f.receiver.id = :senderId)")
	Long findFriendIdsBySenderAndReceiver(Long senderId, Long receiverId);
}
