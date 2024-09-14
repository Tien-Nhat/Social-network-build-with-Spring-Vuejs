package personalproject.social.network.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;
import personalproject.social.network.model.MediaPost;

@Transactional
public interface MediaPostRepository extends JpaRepository<MediaPost, Long> {

	@Modifying
	@Query("UPDATE MediaPost pm SET pm.deleted = true WHERE pm.id IN :idList")
	void deleteByListId(List<Long> idList);

	List<MediaPost> findByPost_IdAndDeletedFalse(Long postId);

	@Query("SELECT mp FROM MediaPost mp JOIN mp.post p WHERE p.user.id = :userId AND mp.contentType LIKE 'image/%' ORDER BY mp.createdDatetime DESC")
	List<MediaPost> findMediaByUserId(Long userId);
}
