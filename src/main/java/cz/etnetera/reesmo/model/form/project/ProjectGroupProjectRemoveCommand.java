package cz.etnetera.reesmo.model.form.project;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

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
