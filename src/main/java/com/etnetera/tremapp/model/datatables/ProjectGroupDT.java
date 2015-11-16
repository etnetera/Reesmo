package com.etnetera.tremapp.model.datatables;

import com.etnetera.tremapp.model.mongodb.project.ProjectGroup;

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

	public void setId(String id) {
		this.id = id;
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
