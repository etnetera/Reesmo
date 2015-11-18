package com.etnetera.tremapp.repository.mongodb.project;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.etnetera.tremapp.model.datatables.project.ProjectGroupDT;
import com.etnetera.tremapp.model.mongodb.project.ProjectGroup;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.repository.mongodb.MongoDatatables;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;

/**
 * Project group repository custom method implementation
 */
public class ProjectGroupRepositoryImpl implements ProjectGroupRepositoryCustom {
	
	@Autowired
	private MongoOperations mongoTemplate;

	@Override
	public List<ProjectGroup> findByUser(String userId) {
		return mongoTemplate.find(Query.query(Criteria.where("users." + userId).exists(true)), ProjectGroup.class);
	}
	
	@Override
	public DataSet<ProjectGroupDT> findWithDatatablesCriterias(DatatablesCriterias criterias, String userId) {
		Criteria allCrit = null;
		if (userId == null) {
			allCrit = Criteria.where("_id").exists(true);
		} else {
			allCrit = Criteria.where("users." + userId).exists(true).and("users." + userId).ne(Permission.NONE);
		}
		Criteria crit = MongoDatatables.getCriteria(criterias, allCrit);

		Query query = Query.query(crit);
		MongoDatatables.sortQuery(query, criterias);
		MongoDatatables.paginateQuery(query, criterias);

		List<ProjectGroup> projectGroups = mongoTemplate.find(query, ProjectGroup.class);

		Long count = mongoTemplate.count(Query.query(allCrit), ProjectGroup.class);
		Long countFiltered = mongoTemplate.count(Query.query(crit), ProjectGroup.class);

		return new DataSet<ProjectGroupDT>(projectGroups.stream().map(pg -> new ProjectGroupDT(pg)).collect(Collectors.toList()),
				count, countFiltered);
	}

}
