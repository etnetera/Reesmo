package cz.etnetera.reesmo.repository.mongodb.user;

import java.util.Locale;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;

import cz.etnetera.reesmo.model.datatables.project.ProjectGroupUserDT;
import cz.etnetera.reesmo.model.datatables.project.ProjectUserDT;
import cz.etnetera.reesmo.model.datatables.project.ProjectUserFromGroupsDT;
import cz.etnetera.reesmo.model.datatables.user.UserDT;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.project.ProjectGroup;
import cz.etnetera.reesmo.model.mongodb.user.User;

/**
 * User repository custom methods
 */
public interface UserRepositoryCustom {
	
	public User findOneById(String id);
	
	public User findOneByUsername(String username);
	
	public DataSet<UserDT> findWithDatatablesCriterias(DatatablesCriterias criterias, Locale locale);
	
	public DataSet<ProjectUserDT> findProjectUsersWithDatatablesCriterias(DatatablesCriterias criterias, Project project, Locale locale);
	
	public DataSet<ProjectUserFromGroupsDT> findProjectUsersFromGroupsWithDatatablesCriterias(DatatablesCriterias criterias, Project project, Locale locale);
	
	public DataSet<ProjectGroupUserDT> findProjectGroupUsersWithDatatablesCriterias(DatatablesCriterias criterias, ProjectGroup projectGroup, Locale locale);
	
}
