package cz.etnetera.reesmo.model.datatables.project;

import cz.etnetera.reesmo.model.datatables.AuditedModelDT;
import cz.etnetera.reesmo.model.mongodb.project.Project;

public class ProjectDT extends AuditedModelDT {

	private String id;
	
	private String name;
	
	private String key;
	
	private String description;
	
	public ProjectDT(Project project) {
		super(project);
		this.id = project.getId();
		this.name = project.getName();
		this.key = project.getKey();
		this.description = project.getDescription();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String getKey() {
		return key;
	}

	public String getDescription() {
		return description;
	}

}
