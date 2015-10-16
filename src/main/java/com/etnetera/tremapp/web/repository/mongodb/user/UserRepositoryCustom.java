package com.etnetera.tremapp.web.repository.mongodb.user;

import com.etnetera.tremapp.web.model.mongodb.user.User;

/**
 * User repository custom methods
 */
public interface UserRepositoryCustom {

	public User findOne(String id);
	
	public User findOneByUsername(String username);
	
}
