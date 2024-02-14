package checkoutapi.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import checkoutapi.entity.TokenRefresh;

@Repository
public interface TokenRefreshRepository extends JpaRepository<TokenRefresh, String> {

}
