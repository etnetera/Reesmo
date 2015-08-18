package com.etnetera.projects.testreporting.webapp.model.mongodb.user;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.etnetera.projects.testreporting.webapp.model.mongodb.MongoAuditedModel;
import com.etnetera.projects.testreporting.webapp.model.mongodb.project.ProjectGroupPermission;

@Document
abstract public class User extends MongoAuditedModel {

	@Id
	private String id;
	
	/**
	 * Is used for filtering mainly.
	 * No need to be unique.
	 */
	private String label;
	
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
	
}
