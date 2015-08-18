package com.etnetera.projects.testreporting.webapp.model.mongodb.project;

import com.etnetera.projects.testreporting.webapp.model.mongodb.MongoAuditedModel;
import com.etnetera.projects.testreporting.webapp.model.mongodb.user.Permission;

public class ProjectGroupPermission extends MongoAuditedModel {

	private ProjectGroup projectGroup;
	
	private Permission permission;

	public ProjectGroup getProjectGroup() {
		return projectGroup;
	}

	public void setProjectGroup(ProjectGroup projectGroup) {
		this.projectGroup = projectGroup;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}
	
}
