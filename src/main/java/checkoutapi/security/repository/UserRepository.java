package checkoutapi.security.repository;

import checkoutapi.entity.User;

//http://blog.netgloo.com/2014/12/18/handling-entities-inheritance-with-spring-data-jpa/
//@Resource
public interface UserRepository extends UserBaseRepository<User> {
}
