package cz.etnetera.reesmo.model.form.view;

import cz.etnetera.reesmo.model.mongodb.view.View;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class ViewCommand {

	@NotBlank
	@Size(min = 2, max = 255)
	protected String name;
	
	protected String description;
	
	public void fromView(View view) {
		name = view.getName();
		description = view.getDescription();
	}
	
	public void toView(View view) {
		view.setName(name);
		view.setDescription(description);
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
