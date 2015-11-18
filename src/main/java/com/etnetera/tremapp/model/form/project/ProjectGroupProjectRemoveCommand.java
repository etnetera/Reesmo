package com.etnetera.tremapp.model.form.project;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

public class ProjectGroupProjectRemoveCommand {
	
	@NotEmpty
	private List<String> projectIds;

	public List<String> getProjectIds() {
		return projectIds;
	}

	public void setProjectIds(List<String> projectIds) {
		this.projectIds = projectIds;
	}

}
