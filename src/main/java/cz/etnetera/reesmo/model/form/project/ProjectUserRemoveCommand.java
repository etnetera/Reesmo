package cz.etnetera.reesmo.model.form.project;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

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
