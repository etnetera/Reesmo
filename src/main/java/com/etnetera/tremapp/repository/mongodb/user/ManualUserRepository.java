package com.etnetera.tremapp.repository.mongodb.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.etnetera.tremapp.model.mongodb.user.ManualUser;

/**
 * Manual User repository
 */
@Repository
public interface ManualUserRepository extends MongoRepository<ManualUser, String>, ManualUserRepositoryCustom {
	
}
