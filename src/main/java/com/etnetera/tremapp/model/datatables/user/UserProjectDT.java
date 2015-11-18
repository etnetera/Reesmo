package com.etnetera.tremapp.model.datatables.user;

import java.util.Locale;

import com.etnetera.tremapp.message.Localizer;
import com.etnetera.tremapp.model.mongodb.project.Project;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.model.mongodb.user.User;

public class UserProjectDT {
	
	private String id;
	
	private String name;
	
	private String key;
	
	private String description;
	
	private String permission;
	
	private String userId;
	
	private String userLabel;
	
	public UserProjectDT(Project project, User user, Permission permission, Localizer localizer, Locale locale) {
		this.id = project.getId();
		this.name = project.getName();
		this.key = project.getKey();
		this.description = project.getDescription();
		this.permission = localizer.localize(permission, locale);
		this.userId = user.getId();
		this.userLabel = user.getLabel();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getKey() {
		return key;
	}

	public String getDescription() {
		return description;
	}

	public String getPermission() {
		return permission;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserLabel() {
		return userLabel;
	}
	
}
