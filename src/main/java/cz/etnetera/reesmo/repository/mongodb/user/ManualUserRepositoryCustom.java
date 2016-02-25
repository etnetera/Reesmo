package cz.etnetera.reesmo.repository.mongodb.user;

import cz.etnetera.reesmo.model.mongodb.user.ManualUser;

/**
 * Manual User repository custom methods
 */
public interface ManualUserRepositoryCustom {
	
	public boolean hasAnyAdmin();
	
	public ManualUser findOneByEmail(String email);
	
}
