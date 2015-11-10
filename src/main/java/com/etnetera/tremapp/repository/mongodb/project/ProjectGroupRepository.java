package com.etnetera.tremapp.repository.mongodb.project;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.etnetera.tremapp.model.mongodb.project.ProjectGroup;

/**
 * Project group repository
 */
@Repository
public interface ProjectGroupRepository extends MongoRepository<ProjectGroup, String>, ProjectGroupRepositoryCustom {
	
}
