package com.etnetera.tremapp.model.form.project;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.etnetera.tremapp.model.mongodb.user.Permission;

public class ProjectMemberAddCommand {

	@NotNull
	private String permission = Permission.NONE.name().toLowerCase();
	
	@NotNull
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
