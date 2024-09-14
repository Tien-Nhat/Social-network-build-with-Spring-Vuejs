package personalproject.social.network.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;
import personalproject.social.network.model.User;
import personalproject.social.network.response.UserDetailListReponse;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String Email);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	@Query(value = "SELECT CONCAT(u.first_name, ' ', u.last_name) FROM Users u WHERE LOWER(CONCAT(u.first_name, ' ', u.last_name)) LIKE LOWER(CONCAT('%', :name, '%')) AND u.username <> 'admin'"
			+ "LIMIT 5", nativeQuery = true)
	List<String> findFullNameContainingIgnoreCase(String name);

	@Query("SELECT u FROM User u WHERE ((CONCAT(u.firstname, ' ', u.lastname) LIKE %:txtSearch%"
			+ " OR CONCAT(u.lastname, ' ', u.firstname) LIKE %:txtSearch%) "
			+ "OR (u.firstname LIKE %:txtSearch% OR u.lastname LIKE %:txtSearch%) "
			+ "OR u.address LIKE %:txtSearch%) AND u.username <> 'admin'")
	List<User> findUsersBySearchCriteria(String txtSearch);

	@Query("SELECT new personalproject.social.network.response.UserDetailListReponse(u.id, u.username, u.firstname, u.lastname, u.email, u.phone, u.address,u.banned)"
			+ "FROM User u WHERE u.username <> 'admin' ORDER BY u.lastname")
	Page<UserDetailListReponse> findAllUserDetails(Pageable pageable);

	@Query("SELECT u.username FROM User u WHERE u.id = :userId")
	String findUsernameById(Long userId);

	@Modifying
	@Query("UPDATE User u SET u.banned = CASE WHEN u.banned = true THEN false ELSE true END WHERE u.id = :userId")
	void toggleBannedStatus(Long userId);

	@Query("SELECT u.banned FROM User u WHERE u.id = :userId")
	Boolean isUserBannedById(Long userId);

	@Query("SELECT new personalproject.social.network.response.UserDetailListReponse(u.id, u.username, u.firstname, u.lastname, u.email, u.phone, u.address,u.banned)"
			+ " FROM User u WHERE u.username <> 'admin' AND u.lastname LIKE %:txtSearch%")
	Page<UserDetailListReponse> searchByLastname(String txtSearch, Pageable pageable);

	@Query("SELECT new personalproject.social.network.response.UserDetailListReponse(u.id, u.username, u.firstname, u.lastname, u.email, u.phone, u.address,u.banned)"
			+ " FROM User u WHERE u.username <> 'admin' AND u.firstname LIKE %:txtSearch%")
	Page<UserDetailListReponse> searchByFirstname(String txtSearch, Pageable pageable);

	@Query("SELECT new personalproject.social.network.response.UserDetailListReponse(u.id, u.username, u.firstname, u.lastname, u.email, u.phone, u.address,u.banned)"
			+ " FROM User u WHERE u.username <> 'admin' AND u.username LIKE %:txtSearch%")
	Page<UserDetailListReponse> searchByUsername(String txtSearch, Pageable pageable);

	@Query("SELECT new personalproject.social.network.response.UserDetailListReponse(u.id, u.username, u.firstname, u.lastname, u.email, u.phone, u.address,u.banned)"
			+ " FROM User u WHERE u.username <> 'admin' AND u.email LIKE %:txtSearch%")
	Page<UserDetailListReponse> searchByEmail(String txtSearch, Pageable pageable);
}
