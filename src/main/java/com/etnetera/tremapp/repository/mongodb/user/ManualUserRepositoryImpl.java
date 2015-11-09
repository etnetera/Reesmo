package com.etnetera.tremapp.repository.mongodb.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.etnetera.tremapp.model.mongodb.user.ManualUser;

/**
 * Manual User repository custom method implementation
 */
public class ManualUserRepositoryImpl implements ManualUserRepositoryCustom {

	@Autowired
    private MongoOperations mongoTemplate;
	
	@Override
	public boolean hasAnyAdmin() {
		return mongoTemplate.count(Query.query(Criteria.where("superadmin").is(true)), ManualUser.class) > 0;
	}
	
}
