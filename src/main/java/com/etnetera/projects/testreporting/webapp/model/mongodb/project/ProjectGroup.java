package com.etnetera.projects.testreporting.webapp.model.mongodb.project;



import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.etnetera.projects.testreporting.webapp.model.mongodb.MongoAuditedModel;

@Document
public class ProjectGroup extends MongoAuditedModel {

	@Id
	private String id;
	
	private String name;
	
	private String description;
	
	@DBRef
	private List<Project> projects;

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

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	
}
