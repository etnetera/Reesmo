package com.etnetera.projects.testreporting.webapp.repository.mongodb.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Repository;

import com.etnetera.projects.testreporting.webapp.model.mongodb.user.User;

/**
 * User repository
 */
@Repository
public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom, UserDetailsService {
	
}
