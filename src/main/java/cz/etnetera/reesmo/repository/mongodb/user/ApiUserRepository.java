package cz.etnetera.reesmo.repository.mongodb.user;

import cz.etnetera.reesmo.model.mongodb.user.ApiUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Api User repository
 */
@Repository
public interface ApiUserRepository extends MongoRepository<ApiUser, String>, ApiUserRepositoryCustom {
	
}
