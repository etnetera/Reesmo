package com.etnetera.tremapp.model.form.project;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

public class ProjectUserRemoveCommand {
	
	@NotEmpty
	private List<String> userIds;

	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}

}
