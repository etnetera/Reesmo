package com.etnetera.tremapp.repository.mongodb.project;

import java.util.ArrayList;
import java.util.List;

import com.etnetera.tremapp.model.mongodb.project.Project;

/**
 * Project repository custom method implementation
 */
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {
	
	private List<Project> projects;

	@Override
	public List<Project> findAll() {
		return getProjects();
	}
	
	@Override
	public Project findOne(String id) {
		for (Project p : getProjects()) {
			if (p.getId().equals(id)) return p;
		}
		return null;
	}

	@Override
	public Project findOneByKey(String key) {
		for (Project p : getProjects()) {
			if (p.getKey() != null && p.getKey().equals(key)) return p;
		}
		return null;
	}
	
	private List<Project> getProjects() {
		if (this.projects == null) {
			List<Project> projects = new ArrayList<>();
			
			for (int i = 1; i <= 2; i++) {
				Project p = new Project();
				p.setId("p" + i);
				p.setKey("P" + i);
				p.setName("Project " + i);
				projects.add(p);
			}
			
			this.projects = projects;
		}
		return this.projects;
	}

}
