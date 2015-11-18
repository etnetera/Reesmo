package com.etnetera.tremapp.model.datatables.project;

import com.etnetera.tremapp.model.datatables.AuditedModelDT;
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

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

}
