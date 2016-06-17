package cz.etnetera.reesmo.model.datatables.project;

import cz.etnetera.reesmo.message.Localizer;
import cz.etnetera.reesmo.model.datatables.user.MemberDT;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.model.mongodb.user.User;

import java.util.Locale;

public class ProjectUserDT extends MemberDT {
	
	private String projectId;
	
	private String projectName;
	
	private String projectKey;
	
	public ProjectUserDT(User user, Project project, Permission permission, Localizer localizer, Locale locale) {
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
