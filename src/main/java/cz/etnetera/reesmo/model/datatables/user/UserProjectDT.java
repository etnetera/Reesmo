package cz.etnetera.reesmo.model.datatables.user;

import cz.etnetera.reesmo.message.Localizer;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.model.mongodb.user.User;

import java.util.Locale;

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
