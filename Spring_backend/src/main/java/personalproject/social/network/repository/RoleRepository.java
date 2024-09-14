package personalproject.social.network.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import personalproject.social.network.model.ERole;
import personalproject.social.network.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	Optional<Role> findByName(ERole name);
}
