package personalproject.social.network.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import personalproject.social.network.model.Post;

@Transactional
public interface PostRepository extends JpaRepository<Post, Long> {

	@Query("SELECT p FROM Post p WHERE p.deleted = false")
	List<Post> findAllNotDeleted();

	List<Post> findByUser_IdAndDeletedFalse(Long userId);

	@Query("SELECT p FROM Post p " + "JOIN p.user u " + "WHERE (p.content LIKE %:txtSearch% "
			+ "OR CONCAT(u.firstname, ' ', u.lastname) LIKE %:txtSearch% "
			+ "OR CONCAT(u.lastname, ' ', u.firstname) LIKE %:txtSearch% )" + "AND p.deleted = false")
	List<Post> findPostsByContentOrUserNameContaining(String txtSearch);

	@Modifying
	@Query("UPDATE Post p SET p.deleted = true WHERE p.id = :id")
	void deletePostById(Long id);

	@Modifying
	@Query("UPDATE Post p SET p.deleted = false WHERE p.id = :id")
	void undeletePostById(Long id);

	@Modifying
	@Query("UPDATE Post p SET p.content = :content, p.createdDatetime = :createdDatetime, p.sharedId=:sharedId WHERE p.id = :id")
	void updatePostContentAndMedia(Long id, String content, String createdDatetime, Long sharedId);
}
