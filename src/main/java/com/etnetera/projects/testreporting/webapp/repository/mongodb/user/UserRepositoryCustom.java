package com.etnetera.projects.testreporting.webapp.repository.mongodb.user;

import com.etnetera.projects.testreporting.webapp.model.mongodb.user.User;

/**
 * User repository custom methods
 */
public interface UserRepositoryCustom {

	public User findOne(String id);
	
}
