package cz.etnetera.reesmo.model.datatables.project;

import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.project.ProjectGroup;

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
