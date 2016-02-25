package cz.etnetera.reesmo.repository.mongodb.project;

import java.util.List;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;

import cz.etnetera.reesmo.model.datatables.project.ProjectGroupDT;
import cz.etnetera.reesmo.model.mongodb.project.ProjectGroup;

/**
 * Project group repository custom methods
 */
public interface ProjectGroupRepositoryCustom {

	public List<ProjectGroup> findByUser(String userId);
	
	public DataSet<ProjectGroupDT> findWithDatatablesCriterias(DatatablesCriterias criterias, String userId);
	
}
