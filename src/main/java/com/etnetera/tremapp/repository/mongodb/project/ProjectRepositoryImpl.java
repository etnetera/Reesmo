package com.etnetera.tremapp.repository.mongodb.project;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.etnetera.tremapp.model.datatables.ProjectDT;
import com.etnetera.tremapp.model.mongodb.project.Project;
import com.etnetera.tremapp.utils.mongo.MongoDatatables;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;

/**
 * Project repository custom method implementation
 */
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {
	
	@Autowired
	private MongoOperations mongoTemplate;
	
	private List<Project> projects;

	@Override
	public List<Project> findAll() {
		return getProjects();
	}
	
	@Override
	public Project findOneById(String id) {
		for (Project p : getProjects()) {
			if (p.getId().equals(id)) return p;
		}
		return null;
	}

	@Override
	public Project findOneByKey(String key) {
		for (Project p : getProjects()) {
			if (p.getKey() != null && p.getKey().equals(key)) return p;
		}
		return null;
	}
	
	private List<Project> getProjects() {
		if (this.projects == null) {
			List<Project> projects = new ArrayList<>();
			
			for (int i = 1; i <= 2; i++) {
				Project p = new Project();
				p.setId("p" + i);
				p.setKey("P" + i);
				p.setName("Project " + i);
				projects.add(p);
			}
			
			this.projects = projects;
		}
		return this.projects;
	}

	@Override
	public DataSet<ProjectDT> findWithDatatablesCriterias(DatatablesCriterias criterias) {
		Criteria crit = MongoDatatables.getCriteria(criterias);
		Criteria allCrit = Criteria.where("_id").exists(true);

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
