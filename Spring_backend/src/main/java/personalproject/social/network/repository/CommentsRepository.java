package personalproject.social.network.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import personalproject.social.network.model.Comments;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
	List<Comments> findByPostId(Long postId);
}
