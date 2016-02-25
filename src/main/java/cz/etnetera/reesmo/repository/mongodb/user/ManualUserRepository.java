package cz.etnetera.reesmo.repository.mongodb.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import cz.etnetera.reesmo.model.mongodb.user.ManualUser;

/**
 * Manual User repository
 */
@Repository
public interface ManualUserRepository extends MongoRepository<ManualUser, String>, ManualUserRepositoryCustom {
	
}
