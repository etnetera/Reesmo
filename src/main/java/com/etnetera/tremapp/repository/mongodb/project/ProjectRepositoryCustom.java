package com.etnetera.tremapp.repository.mongodb.project;

import java.util.List;

import com.etnetera.tremapp.model.mongodb.project.Project;

/**
 * Project repository custom methods
 */
public interface ProjectRepositoryCustom {

	public List<Project> findAll();
	
	public Project findOne(String id);
	
	public Project findOneByKey(String key);
	
}
