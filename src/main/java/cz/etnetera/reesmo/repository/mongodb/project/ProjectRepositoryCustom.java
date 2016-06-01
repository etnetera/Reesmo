package cz.etnetera.reesmo.repository.mongodb.project;

import java.util.List;
import java.util.Locale;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;

import cz.etnetera.reesmo.model.datatables.project.ProjectDT;
import cz.etnetera.reesmo.model.datatables.project.ProjectGroupProjectDT;
import cz.etnetera.reesmo.model.datatables.user.UserProjectDT;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.project.ProjectGroup;
import cz.etnetera.reesmo.model.mongodb.user.User;

/**
 * Project repository custom methods
 */
public interface ProjectRepositoryCustom {
	
	public void deleteProject(Project project);
	
	public Project findOneByKey(String key);
	
	public List<Project> findByUser(String userId);
	
	public DataSet<ProjectDT> findWithDatatablesCriterias(DatatablesCriterias criterias, List<String> projectIds);
	
	public DataSet<ProjectGroupProjectDT> findProjectGroupProjectsWithDatatablesCriterias(DatatablesCriterias criterias,
			ProjectGroup projectGroup);
	
	public DataSet<UserProjectDT> findUserProjectsWithDatatablesCriterias(DatatablesCriterias criterias,
			User user, Locale locale);
	
}
