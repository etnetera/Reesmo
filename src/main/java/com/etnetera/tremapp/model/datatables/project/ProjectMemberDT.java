package com.etnetera.tremapp.model.datatables.project;

import java.util.Locale;

import com.etnetera.tremapp.message.Localizer;
import com.etnetera.tremapp.model.datatables.user.MemberDT;
import com.etnetera.tremapp.model.mongodb.project.Project;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.model.mongodb.user.User;

public class ProjectMemberDT extends MemberDT {
	
	private String projectId;
	
	private String projectName;
	
	private String projectKey;
	
	public ProjectMemberDT(User user, Project project, Permission permission, Localizer localizer, Locale locale) {
		super(user, permission, localizer, locale);
		this.projectId = project.getId();
		this.projectName = project.getName();
		this.projectKey = project.getKey();
	}

	public String getProjectId() {
		return projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getProjectKey() {
		return projectKey;
	}

}
