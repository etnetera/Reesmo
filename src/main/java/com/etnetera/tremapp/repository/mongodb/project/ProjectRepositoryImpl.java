package com.etnetera.tremapp.repository.mongodb.project;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.etnetera.tremapp.model.datatables.project.ProjectDT;
import com.etnetera.tremapp.model.mongodb.project.Project;
import com.etnetera.tremapp.repository.mongodb.MongoDatatables;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;

/**
 * Project repository custom method implementation
 */
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {
	
	@Autowired
	private MongoOperations mongoTemplate;

	@Override
	public Project findOneByKey(String key) {
		return mongoTemplate.findOne(Query.query(Criteria.where("key").is(key)), Project.class);
	}

	@Override
	public DataSet<ProjectDT> findWithDatatablesCriterias(DatatablesCriterias criterias, List<String> projectIds) {
		Criteria allCrit = null;
		if (projectIds == null) {
			allCrit = Criteria.where("_id").exists(true);
		} else if (projectIds.isEmpty()) {
			return new DataSet<ProjectDT>(new ArrayList<>(), 0L, 0L);
		} else {
			allCrit = Criteria.where("_id").in(projectIds);
		}
		
		Criteria crit = MongoDatatables.getCriteria(criterias, allCrit);

		Query query = Query.query(crit);
		MongoDatatables.sortQuery(query, criterias);
		MongoDatatables.paginateQuery(query, criterias);

		List<Project> projects = mongoTemplate.find(query, Project.class);

		Long count = mongoTemplate.count(Query.query(allCrit), Project.class);
		Long countFiltered = mongoTemplate.count(Query.query(crit), Project.class);

		return new DataSet<ProjectDT>(projects.stream().map(p -> new ProjectDT(p)).collect(Collectors.toList()),
				count, countFiltered);
	}

}
