package cz.etnetera.reesmo.model.form.project;

import cz.etnetera.reesmo.model.mongodb.project.Project;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ProjectCommand {

	@NotBlank
	@Size(min = 2, max = 255)
	protected String name;
	
	@Pattern(regexp = "^[A-Za-z0-9_\\-\\.]*$", message = "{validator.Pattern.alphaNumericUnderscoreDashDot.message}")
	@Size(max = 255)
	private String key;
	
	protected String description;
	
	public void fromProject(Project project) {
		name = project.getName();
		key = project.getKey();
		description = project.getDescription();
	}
	
	public void toProject(Project project) {
		project.setName(name);
		project.setKey(key);
		project.setDescription(description);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
