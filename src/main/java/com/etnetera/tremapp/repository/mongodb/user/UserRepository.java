package com.etnetera.tremapp.repository.mongodb.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.etnetera.tremapp.model.mongodb.user.User;

/**
 * User repository
 */
@Repository
public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {
	
}
