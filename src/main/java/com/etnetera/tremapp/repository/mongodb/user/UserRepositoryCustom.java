package com.etnetera.tremapp.repository.mongodb.user;

import java.util.Locale;

import com.etnetera.tremapp.model.datatables.project.ProjectMemberDT;
import com.etnetera.tremapp.model.datatables.project.ProjectMemberFromGroupsDT;
import com.etnetera.tremapp.model.datatables.user.UserDT;
import com.etnetera.tremapp.model.mongodb.project.Project;
import com.etnetera.tremapp.model.mongodb.user.User;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;

/**
 * User repository custom methods
 */
public interface UserRepositoryCustom {
	
	public User findOneById(String id);
	
	public User findOneByUsername(String username);
	
	public DataSet<UserDT> findWithDatatablesCriterias(DatatablesCriterias criterias, Locale locale);
	
	public DataSet<ProjectMemberDT> findProjectMembersWithDatatablesCriterias(DatatablesCriterias criterias, Project project, Locale locale);
	
	public DataSet<ProjectMemberFromGroupsDT> findProjectMembersFromGroupsWithDatatablesCriterias(DatatablesCriterias criterias, Project project, Locale locale);
	
}
