package com.etnetera.tremapp.model.form.project;

import java.util.List;

import javax.validation.constraints.NotNull;

public class ProjectMemberRemoveCommand {
	
	@NotNull
	private List<String> userIds;

	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}

}
