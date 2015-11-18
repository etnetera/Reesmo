package com.etnetera.tremapp.model.form.project;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.etnetera.tremapp.model.mongodb.user.Permission;

public class ProjectUserAddCommand {

	@NotEmpty
	private String permission = Permission.NONE.name().toLowerCase();
	
	@NotEmpty
	private List<String> userIds;

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}

}
