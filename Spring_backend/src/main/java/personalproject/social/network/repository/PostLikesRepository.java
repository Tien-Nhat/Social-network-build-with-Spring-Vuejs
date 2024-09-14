package personalproject.social.network.repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import personalproject.social.network.model.PostLikes;
import personalproject.social.network.model.User;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {

	List<PostLikes> findByUserId(Long userId);

	boolean existsByUserIdAndPostId(Long userId, Long postId);

	PostLikes findByUserIdAndPostId(Long userId, Long postId);

	@Query("SELECT pl.post.id, pl.liked FROM PostLikes pl WHERE pl.user.id = :userId")
	List<Object[]> findByUser_Id(Long userId);

	default Map<Long, Boolean> findLikedByUserId(Long userId) {
		return findByUser_Id(userId).stream().collect(Collectors.toMap(obj -> (Long) obj[0], // postId
				obj -> (Boolean) obj[1] // liked
		));
	}

	@Query("SELECT pl.user FROM PostLikes pl WHERE pl.post.id = :postId AND pl.liked = true ORDER BY CASE WHEN pl.user.id = :userId THEN 0 ELSE 1 END, pl.likeDate DESC")
	List<User> findUsersLikedPost(Long postId, Long userId);

	@Query("SELECT pl.liked FROM PostLikes pl WHERE pl.post.id = :postId AND pl.user.id = :userId")
	Boolean findLikedByPostIdAndUserId(Long postId, Long userId);
}
