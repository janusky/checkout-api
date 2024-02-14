package checkoutapi.confg;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/*
 * https://www.baeldung.com/spring-cache-tutorial 
 * 	https://www.baeldung.com/spring-boot-ehcache
 * 	https://www.baeldung.com/spring-boot-evict-cache
 * 	https://springframework.guru/using-ehcache-3-in-spring-boot/
 */
@Configuration
@EnableCaching
public class CacheConfig {
}
