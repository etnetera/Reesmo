package cz.etnetera.reesmo.model.datatables.project;

import java.util.Locale;

import cz.etnetera.reesmo.message.Localizer;
import cz.etnetera.reesmo.model.datatables.user.MemberDT;
import cz.etnetera.reesmo.model.mongodb.project.ProjectGroup;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.model.mongodb.user.User;

public class ProjectGroupUserDT extends MemberDT {
	
	private String projectGroupId;
	
	private String projectGroupName;
	
	public ProjectGroupUserDT(User user, ProjectGroup projectGroup, Permission permission, Localizer localizer, Locale locale) {
		super(user, permission, localizer, locale);
		this.projectGroupId = projectGroup.getId();
		this.projectGroupName = projectGroup.getName();
	}

	public String getProjectGroupId() {
		return projectGroupId;
	}

	public String getProjectGroupName() {
		return projectGroupName;
	}
	
}
