package checkoutapi.security.repository;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import checkoutapi.entity.Role;
import checkoutapi.entity.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	static final String ROLE_findByName = "ROLE_findByNameCACHE";
	
	@Cacheable(value = ROLE_findByName, key = "#p0", unless = "#result == null")
	Optional<Role> findByName(RoleName roleName);
}
