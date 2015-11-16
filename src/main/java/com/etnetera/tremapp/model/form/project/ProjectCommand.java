package com.etnetera.tremapp.model.form.project;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.etnetera.tremapp.model.mongodb.project.Project;

public class ProjectCommand {

	@Size(max = 255)
	private String key;

	@NotBlank
	@Size(min = 2, max = 255)
	protected String name;
	
	protected String description;
	
	public void fromProject(Project project) {
		key = project.getKey();
		name = project.getName();
		description = project.getDescription();
	}
	
	public void toProject(Project project) {
		project.setKey(key);
		project.setName(name);
		project.setDescription(description);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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
