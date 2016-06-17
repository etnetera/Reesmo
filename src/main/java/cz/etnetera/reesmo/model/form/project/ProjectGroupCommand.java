package cz.etnetera.reesmo.model.form.project;

import cz.etnetera.reesmo.model.mongodb.project.ProjectGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class ProjectGroupCommand {
	
	@NotBlank
	@Size(min = 2, max = 255)
	protected String name;
	
	protected String description;
	
	public void fromProject(ProjectGroup projectGroup) {
		name = projectGroup.getName();
		description = projectGroup.getDescription();
	}
	
	public void toProject(ProjectGroup projectGroup) {
		projectGroup.setName(name);
		projectGroup.setDescription(description);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
