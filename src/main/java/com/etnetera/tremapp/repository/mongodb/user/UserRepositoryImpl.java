package com.etnetera.tremapp.repository.mongodb.user;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.etnetera.tremapp.model.datatables.UserDT;
import com.etnetera.tremapp.model.mongodb.user.ManualUser;
import com.etnetera.tremapp.model.mongodb.user.User;
import com.etnetera.tremapp.repository.mongodb.MongoDatatables;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;

/**
 * User repository custom method implementation
 */
public class UserRepositoryImpl implements UserRepositoryCustom {

	@Autowired
	private MongoOperations mongoTemplate;

	@Autowired
	private ManualUserRepository manualUserRepository;

	@PostConstruct
	private void init() {
		// create super admin if there is no one
		if (!manualUserRepository.hasAnyAdmin()) {
			ManualUser u = new ManualUser();
			u.setLabel("Super Admin");
			u.setUsername("admin");
			u.setPassword(new BCryptPasswordEncoder().encode("admin"));
			u.setEmail("admin@tremapp.local");
			u.setActive(true);
			u.setSuperadmin(true);
			manualUserRepository.save(u);
		}
	}

	@Override
	public User findOneById(String id) {
		return mongoTemplate.findOne(Query.query(Criteria.where("_id").is(id)), User.class);
	}

	@Override
	public User findOneByUsername(String username) {
		return mongoTemplate.findOne(Query.query(Criteria.where("username").is(username)), User.class);
	}

	@Override
	public DataSet<UserDT> findWithDatatablesCriterias(DatatablesCriterias criterias) {
		Criteria crit = MongoDatatables.getCriteria(criterias);
		Criteria allCrit = Criteria.where("_id").exists(true);

		Query query = Query.query(crit);
		MongoDatatables.sortQuery(query, criterias);
		MongoDatatables.paginateQuery(query, criterias);

		List<User> users = mongoTemplate.find(query, User.class);

		Long count = mongoTemplate.count(Query.query(allCrit), User.class);
		Long countFiltered = mongoTemplate.count(Query.query(crit), User.class);

		return new DataSet<UserDT>(users.stream().map(u -> new UserDT(u)).collect(Collectors.toList()),
				count, countFiltered);
	}

}
