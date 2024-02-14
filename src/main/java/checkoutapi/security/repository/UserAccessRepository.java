package checkoutapi.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import checkoutapi.entity.UserAccess;

@Repository
public interface UserAccessRepository extends JpaRepository<UserAccess, Long> {

}
