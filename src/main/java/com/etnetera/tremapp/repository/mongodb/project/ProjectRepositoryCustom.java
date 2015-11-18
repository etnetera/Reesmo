package com.etnetera.tremapp.repository.mongodb.project;

import java.util.List;

import com.etnetera.tremapp.model.datatables.project.ProjectDT;
import com.etnetera.tremapp.model.datatables.project.ProjectGroupProjectDT;
import com.etnetera.tremapp.model.mongodb.project.Project;
import com.etnetera.tremapp.model.mongodb.project.ProjectGroup;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;

/**
 * Project repository custom methods
 */
public interface ProjectRepositoryCustom {
	
	public Project findOneByKey(String key);
	
	public List<Project> findByUser(String userId);
	
	public DataSet<ProjectDT> findWithDatatablesCriterias(DatatablesCriterias criterias, List<String> projectIds);
	
	public DataSet<ProjectGroupProjectDT> findProjectGroupProjectsWithDatatablesCriterias(DatatablesCriterias criterias,
			ProjectGroup projectGroup);
	
}
