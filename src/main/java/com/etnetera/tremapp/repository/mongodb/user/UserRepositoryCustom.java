package com.etnetera.tremapp.repository.mongodb.user;

import com.etnetera.tremapp.model.mongodb.user.User;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;

/**
 * User repository custom methods
 */
public interface UserRepositoryCustom {
	
	public User findOneById(String id);
	
	public User findOneByUsername(String username);
	
	public DataSet<User> findWithDatatablesCriterias(DatatablesCriterias criterias);
	
}
