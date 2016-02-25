package cz.etnetera.reesmo.model.form.project;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import cz.etnetera.reesmo.model.mongodb.user.Permission;

public class ProjectUserAddCommand {

	@NotEmpty
	private String permission = Permission.BASIC.name().toLowerCase();
	
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
