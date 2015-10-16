package com.etnetera.tremapp.web.repository.mongodb.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.etnetera.tremapp.web.model.mongodb.user.User;

/**
 * User repository
 */
@Repository
public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {
	
}
