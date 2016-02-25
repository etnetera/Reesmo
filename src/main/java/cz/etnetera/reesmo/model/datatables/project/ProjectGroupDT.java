package cz.etnetera.reesmo.model.datatables.project;

import cz.etnetera.reesmo.model.datatables.AuditedModelDT;
import cz.etnetera.reesmo.model.mongodb.project.ProjectGroup;

public class ProjectGroupDT extends AuditedModelDT {

	private String id;
	
	private String name;
	
	private String description;
	
	public ProjectGroupDT(ProjectGroup projectGroup) {
		super(projectGroup);
		this.id = projectGroup.getId();
		this.name = projectGroup.getName();
		this.description = projectGroup.getDescription();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

}
