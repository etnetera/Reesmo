package com.etnetera.tremapp.repository.mongodb.project;

import java.util.List;

import com.etnetera.tremapp.model.datatables.project.ProjectGroupDT;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;

/**
 * Project group repository custom methods
 */
public interface ProjectGroupRepositoryCustom {

	public DataSet<ProjectGroupDT> findWithDatatablesCriterias(DatatablesCriterias criterias, List<String> projectGroupIds);
	
}
