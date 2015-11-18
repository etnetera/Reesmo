package com.etnetera.tremapp.model.datatables.project;

import com.etnetera.tremapp.model.mongodb.project.Project;
import com.etnetera.tremapp.model.mongodb.project.ProjectGroup;

public class ProjectGroupProjectDT {
	
	private String id;
	
	private String name;
	
	private String key;
	
	private String description;
	
	private String projectGroupId;
	
	private String projectGroupName;
	
	public ProjectGroupProjectDT(Project project, ProjectGroup projectGroup) {
		this.id = project.getId();
		this.name = project.getName();
		this.key = project.getKey();
		this.description = project.getDescription();
		this.projectGroupId = projectGroup.getId();
		this.projectGroupName = projectGroup.getName();
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

	public String getProjectGroupId() {
		return projectGroupId;
	}

	public String getProjectGroupName() {
		return projectGroupName;
	}
	
}
