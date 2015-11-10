package com.etnetera.tremapp.model.datatables;

import com.etnetera.tremapp.model.mongodb.project.Project;

public class ProjectDT extends AuditedModelDT {

	private String id;
	
	private String key;
	
	private String name;
	
	private String description;
	
	public ProjectDT(Project project) {
		super(project);
		this.id = project.getId();
		this.key = project.getKey();
		this.name = project.getName();
		this.description = project.getDescription();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
