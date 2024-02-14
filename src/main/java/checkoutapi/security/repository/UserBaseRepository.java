package checkoutapi.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import checkoutapi.entity.User;

//http://blog.netgloo.com/2014/12/18/handling-entities-inheritance-with-spring-data-jpa/
//@NoRepositoryBean
@CacheConfig(cacheNames = { UserBaseRepository.CACHE })
public interface UserBaseRepository<T extends User> extends JpaRepository<T, Long> {
	static final String CACHE = "User";
	static final String CACHE_findById = CACHE + "findById";
	static final String CACHE_findByUsernameOrEmail = CACHE +  "findByUsernameOrEmail";

    Optional<T> findByEmail(String email);

	@Cacheable(value = CACHE_findByUsernameOrEmail, key = "{#p0,#p1}", unless = "#result == null")
    Optional<T> findByUsernameOrEmail(String username, String email);

    List<T> findByIdIn(List<Long> userIds);

    Optional<T> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
    
    Boolean existsByUsernameAndIdNot(String username, Long id);
    
    Boolean existsByEmailAndIdNot(String email, Long id);
    
	@Cacheable(value = CACHE_findById, key = "#p0", unless = "#result == null")
	@EntityGraph(attributePaths = { "roles" })
    Optional<T> findById(Long id);
    
	@Caching( 
		evict = {
			@CacheEvict(value = CACHE_findByUsernameOrEmail, allEntries = true),
			@CacheEvict(value = CACHE_findById, allEntries = true)
		}
	)
	@Transactional
    <S extends T> S save(S entity);

	@Caching( 
		evict = {
			@CacheEvict(value = CACHE_findByUsernameOrEmail, allEntries = true),
			@CacheEvict(value = CACHE_findById, allEntries = true)
		}
	)
	@Transactional
    default <S extends T> S update(S entity) {
    	return save(entity);
    }

	@Transactional
	Optional<T> findByVerifyCodeAndUsername(String verifyCode, String username);
}
