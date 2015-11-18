package com.etnetera.tremapp.repository.mongodb.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.etnetera.tremapp.message.Localizer;
import com.etnetera.tremapp.model.datatables.project.ProjectDT;
import com.etnetera.tremapp.model.datatables.project.ProjectGroupProjectDT;
import com.etnetera.tremapp.model.datatables.user.UserProjectDT;
import com.etnetera.tremapp.model.mongodb.project.Project;
import com.etnetera.tremapp.model.mongodb.project.ProjectGroup;
import com.etnetera.tremapp.model.mongodb.user.User;
import com.etnetera.tremapp.repository.mongodb.MongoDatatables;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;

/**
 * Project repository custom method implementation
 */
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {
	
	@Autowired
	private MongoOperations mongoTemplate;
	
	@Autowired
	private Localizer localizer;

	@Override
	public Project findOneByKey(String key) {
		return mongoTemplate.findOne(Query.query(Criteria.where("key").is(key)), Project.class);
	}
	
	@Override
	public List<Project> findByUser(String userId) {
		return mongoTemplate.find(Query.query(Criteria.where("users." + userId).exists(true)), Project.class);
	}

	@Override
	public DataSet<ProjectDT> findWithDatatablesCriterias(DatatablesCriterias criterias, List<String> projectIds) {
		DataSet<Project> projects = findProjectsWithDatatablesCriterias(criterias, projectIds);

		return new DataSet<ProjectDT>(
				projects.getRows().stream().map(p -> new ProjectDT(p)).collect(Collectors.toList()),
				projects.getTotalRecords(), projects.getTotalDisplayRecords());
	}

	@Override
	public DataSet<ProjectGroupProjectDT> findProjectGroupProjectsWithDatatablesCriterias(DatatablesCriterias criterias,
			ProjectGroup projectGroup) {
		DataSet<Project> projects = findProjectsWithDatatablesCriterias(criterias, projectGroup.getProjects());

		return new DataSet<ProjectGroupProjectDT>(projects.getRows().stream().map(p -> {
			return new ProjectGroupProjectDT(p, projectGroup);
		}).collect(Collectors.toList()), projects.getTotalRecords(), projects.getTotalDisplayRecords());
	}
	
	@Override
	public DataSet<UserProjectDT> findUserProjectsWithDatatablesCriterias(DatatablesCriterias criterias, User user, Locale locale) {
		DataSet<Project> projects = findProjectsWithDatatablesCriterias(criterias, user.getProjectsPermissions().keySet());

		return new DataSet<UserProjectDT>(projects.getRows().stream().map(p -> {
			return new UserProjectDT(p, user, user.getProjectsPermissions().get(p.getId()), localizer, locale);
		}).collect(Collectors.toList()), projects.getTotalRecords(), projects.getTotalDisplayRecords());
	}
	
	private DataSet<Project> findProjectsWithDatatablesCriterias(DatatablesCriterias criterias, Collection<String> projectIds) {
		Criteria allCrit = null;
		if (projectIds == null) {
			allCrit = Criteria.where("_id").exists(true);
		} else if (projectIds.isEmpty()) {
			return new DataSet<Project>(new ArrayList<>(), 0L, 0L);
		} else {
			allCrit = Criteria.where("_id").in(projectIds);
		}

		Criteria crit = MongoDatatables.getCriteria(criterias, allCrit);

		Long count = mongoTemplate.count(Query.query(allCrit), User.class);
		Long countFiltered = mongoTemplate.count(Query.query(crit), User.class);
		
		Query query = Query.query(crit);
		MongoDatatables.sortQuery(query, criterias);
		MongoDatatables.paginateQuery(query, criterias);

		List<Project> users = mongoTemplate.find(query, Project.class);

		return new DataSet<Project>(users, count, countFiltered);
	}

}
