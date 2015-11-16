package com.etnetera.tremapp.repository.mongodb.project;

import com.etnetera.tremapp.model.datatables.ProjectDT;
import com.etnetera.tremapp.model.mongodb.project.Project;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;

/**
 * Project repository custom methods
 */
public interface ProjectRepositoryCustom {
	
	public Project findOneByKey(String key);
	
	public DataSet<ProjectDT> findWithDatatablesCriterias(DatatablesCriterias criterias);
	
}
