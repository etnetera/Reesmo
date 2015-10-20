package com.etnetera.tremapp.repository.mongodb.project;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.etnetera.tremapp.model.mongodb.project.Project;

/**
 * Project repository
 */
@Repository
public interface ProjectRepository extends MongoRepository<Project, String>, ProjectRepositoryCustom {
	
}
