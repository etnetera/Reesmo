package com.etnetera.tremapp.model.mongodb.project;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.etnetera.tremapp.model.mongodb.MongoAuditedModel;
import com.etnetera.tremapp.model.mongodb.user.Permission;

@Document
public class ProjectGroup extends MongoAuditedModel {

	@Id
	private String id;
	
	private String name;
	
	private String description;
	
	private List<String> projects;
	
	private Map<String, Permission> members;

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

	public List<String> getProjects() {
		return projects;
	}

	public void setProjects(List<String> projects) {
		this.projects = projects;
	}

	public Map<String, Permission> getMembers() {
		return members;
	}

	public void setMembers(Map<String, Permission> members) {
		this.members = members;
	}
	
}
