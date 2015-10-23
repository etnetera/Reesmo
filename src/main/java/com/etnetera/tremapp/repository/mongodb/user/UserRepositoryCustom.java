package com.etnetera.tremapp.repository.mongodb.user;

import java.util.List;

import com.etnetera.tremapp.model.mongodb.user.User;

/**
 * User repository custom methods
 */
public interface UserRepositoryCustom {

	public List<User> findAll();
	
	public User findOneById(String id);
	
	public User findOneByUsername(String username);
	
}
