package com.etnetera.tremapp.repository.mongodb.user;

import com.etnetera.tremapp.model.mongodb.user.ManualUser;

/**
 * Manual User repository custom methods
 */
public interface ManualUserRepositoryCustom {
	
	public boolean hasAnyAdmin();
	
	public ManualUser findOneByEmail(String email);
	
}
