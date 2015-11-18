package com.etnetera.tremapp.model.datatables.project;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.etnetera.tremapp.message.Localizer;
import com.etnetera.tremapp.model.datatables.user.MemberDT;
import com.etnetera.tremapp.model.mongodb.project.Project;
import com.etnetera.tremapp.model.mongodb.project.ProjectGroup;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.model.mongodb.user.User;

public class ProjectUserFromGroupsDT extends MemberDT {
	
	private String projectId;
	
	private String projectName;
	
	private String projectKey;
	
	private String projectGroupsNames;
	
	public ProjectUserFromGroupsDT(User user, Project project, Permission permission, List<ProjectGroup> projectGroups, Localizer localizer, Locale locale) {
		super(user, permission, localizer, locale);
		this.projectId = project.getId();
		this.projectName = project.getName();
		this.projectKey = project.getKey();
		this.projectGroupsNames = String.join(", ", projectGroups.stream().map(pg -> pg.getName()).collect(Collectors.toList()));
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

	public String getProjectGroupsNames() {
		return projectGroupsNames;
	}
	
}
