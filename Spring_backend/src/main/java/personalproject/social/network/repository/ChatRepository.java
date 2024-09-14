package personalproject.social.network.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import personalproject.social.network.model.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {
	@Query("SELECT c FROM Chat c WHERE (c.sender.id = :senderId AND c.receiver.id = :receiverId) OR (c.sender.id = :receiverId AND c.receiver.id = :senderId) ORDER BY c.messageDate ASC")
	List<Chat> findChatsBySenderAndReceiverOrderByMessageDateAsc(Long senderId, Long receiverId);
}
