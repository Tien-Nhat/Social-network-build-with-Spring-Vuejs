package personalproject.social.network.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import personalproject.social.network.model.Notification;

@Transactional
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findByReceiver_IdOrderByNotificationDateDesc(Long userId);

	Long countByReceiver_IdAndIsReadFalse(Long userId);

	@Modifying
	@Query("UPDATE Notification n SET n.isRead = true WHERE n.id = :id")
	void setFalseIsReadById(Long id);
}
