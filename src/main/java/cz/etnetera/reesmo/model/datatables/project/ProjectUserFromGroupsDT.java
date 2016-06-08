package cz.etnetera.reesmo.model.datatables.project;

import cz.etnetera.reesmo.message.Localizer;
import cz.etnetera.reesmo.model.datatables.user.MemberDT;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.project.ProjectGroup;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.model.mongodb.user.User;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
