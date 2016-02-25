package cz.etnetera.reesmo.repository.mongodb.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import cz.etnetera.reesmo.model.mongodb.user.User;

/**
 * User repository
 */
@Repository
public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {
	
}
