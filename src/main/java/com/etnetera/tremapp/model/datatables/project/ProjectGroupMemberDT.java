package com.etnetera.tremapp.model.datatables.project;

import java.util.Locale;

import com.etnetera.tremapp.message.Localizer;
import com.etnetera.tremapp.model.datatables.user.MemberDT;
import com.etnetera.tremapp.model.mongodb.project.ProjectGroup;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.model.mongodb.user.User;

public class ProjectGroupMemberDT extends MemberDT {
	
	private String projectGroupId;
	
	private String projectGroupName;
	
	public ProjectGroupMemberDT(User user, ProjectGroup projectGroup, Permission permission, Localizer localizer, Locale locale) {
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
