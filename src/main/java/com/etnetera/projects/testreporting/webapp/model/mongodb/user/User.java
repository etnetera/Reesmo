package com.etnetera.projects.testreporting.webapp.model.mongodb.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.etnetera.projects.testreporting.webapp.model.mongodb.MongoAuditedModel;
import com.etnetera.projects.testreporting.webapp.model.mongodb.project.Project;
import com.etnetera.projects.testreporting.webapp.model.mongodb.project.ProjectGroupPermission;
import com.etnetera.projects.testreporting.webapp.user.ForbiddenException;

@Document(collection = "user")
abstract public class User extends MongoAuditedModel {

	@Id
	private String id;
	
	/**
	 * Is used for filtering mainly.
	 * No need to be unique.
	 */
	private String label;
	
	@Indexed(unique = true)
	private String username;
	
	private String password;
	
	/**
	 * If true than this user is allowed for everything
	 * and is not affected with permissions.
	 */
	private boolean superadmin;
	
	private List<ProjectGroupPermission> permissions;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isSuperadmin() {
		return superadmin;
	}

	public void setSuperadmin(boolean superadmin) {
		this.superadmin = superadmin;
	}

	public List<ProjectGroupPermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<ProjectGroupPermission> permissions) {
		this.permissions = permissions;
	}
	
	public Map<Project, Permission> getProjectsPermissions() {
		Map<Project, Permission> perms = new HashMap<>();
		permissions.forEach(pgp -> {
			pgp.getProjectGroup().getProjects().forEach(p -> {
				Permission pPerm = perms.get(p);
				if (pPerm == null || pgp.getPermission().isGreaterThan(pPerm)) {
					perms.put(p, pgp.getPermission());
				}
			});
		});
		return perms;
	}
	
	public List<Project> getAllowedProjects(Permission permission) {
		if (superadmin) {
			return null;
		}
		List<Project> projects = new ArrayList<>();
		if (permission != null) {
			for (Map.Entry<Project, Permission> entry : getProjectsPermissions().entrySet()) {
				if (permission.isGreaterThanOrEqual(entry.getValue())) {
					projects.add(entry.getKey());
				}
			}
		}
		return projects;
	}
	
	public List<String> getAllowedProjectIds(Permission permission) {
		List<Project> projects = getAllowedProjects(permission);
		if (projects == null) {
			return null;
		}
		List<String> projectIds = new ArrayList<>();
		projects.forEach(p -> projectIds.add(p.getId()));
		return projectIds;
	}
	
	public void checkProjectPermission(Project project, Permission permission) {
		checkProjectPermission(project.getId(), permission);
	}
	
	public void checkProjectPermission(String projectId, Permission permission) {
		if (!isAllowedForProject(projectId, permission)) {
			throw new ForbiddenException("User with id " + id + " has not " + permission + " permission for project with id " + projectId + ".");
		}
	}
	
	public boolean isAllowedForProject(Project project, Permission permission) {
		return isAllowedForProject(project.getId(), permission);
	}
	
	public boolean isAllowedForProject(String projectId, Permission permission) {
		if (superadmin) {
			return true;
		}
		if (projectId != null && permission != null) {
			for (Map.Entry<Project, Permission> entry : getProjectsPermissions().entrySet()) {
				if (entry.getKey().getId().equals(projectId)) {
					return entry.getValue().isGreaterThanOrEqual(permission);
				}
			}
		}
		return false;
	}
	
}
