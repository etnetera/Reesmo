package com.etnetera.tremapp.model.mongodb.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import com.etnetera.tremapp.http.exception.ForbiddenException;
import com.etnetera.tremapp.model.mongodb.MongoAuditedModel;
import com.etnetera.tremapp.user.UserType;

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
	 * User can be deactivated but still kept in app.
	 */
	private boolean enabled;
	
	/**
	 * If true than this user is allowed for everything
	 * and is not affected with permissions.
	 */
	private boolean superadmin;
	
	/**
	 * Compound project and project group permissions.
	 * Key is project id.
	 */
	private Map<String, Permission> projectsPermissions = new HashMap<>();
	
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isSuperadmin() {
		return superadmin;
	}

	public void setSuperadmin(boolean superadmin) {
		this.superadmin = superadmin;
	}
	
	public Map<String, Permission> getProjectsPermissions() {
		return projectsPermissions;
	}

	public void setProjectsPermissions(Map<String, Permission> projectsPermissions) {
		this.projectsPermissions = projectsPermissions;
	}

	public List<String> getAllowedProjectsIds(Permission permission) {
		if (superadmin) {
			return null;
		}
		List<String> projectsIds = new ArrayList<>();
		if (permission != null) {
			for (Map.Entry<String, Permission> entry : getProjectsPermissions().entrySet()) {
				if (entry.getValue().isGreaterThanOrEqual(permission)) {
					projectsIds.add(entry.getKey());
				}
			}
		}
		return projectsIds;
	}
	
	public void checkProjectPermission(String projectId, Permission permission) {
		if (!isAllowedForProject(projectId, permission)) {
			throw new ForbiddenException("User with id " + id + " has not " + permission + " permission for project with id " + projectId + ".");
		}
	}
	
	public boolean isAllowedForProject(String projectId, Permission permission) {
		if (superadmin) {
			return true;
		}
		if (projectId != null && permission != null) {
			for (Map.Entry<String, Permission> entry : getProjectsPermissions().entrySet()) {
				if (entry.getKey().equals(projectId)) {
					return entry.getValue().isGreaterThanOrEqual(permission);
				}
			}
		}
		return false;
	}
	
	public void checkUserPermission(User user, Permission permission) {
		if (!isAllowedForUser(user, permission)) {
			throw new ForbiddenException("User with id " + user == null ? null
					: user.getId() + " has not " + permission + " permission for user with id " + getId() + ".");
		}
	}

	public boolean isAllowedForUser(User user, Permission permission) {
		if (user == null) {
			return false;
		}
		if (user.isSuperadmin()) {
			return true;
		}
		if (permission == null) {
			return false;
		}
		if (permission.isGreaterThan(Permission.EDITOR)) {
			return false;
		}
		return user.getId().equals(getId());
	}
	
	public abstract String getRole();
	
	public abstract UserType getType();
	
}
