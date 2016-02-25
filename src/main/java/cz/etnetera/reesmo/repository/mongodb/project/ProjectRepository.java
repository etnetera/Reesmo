package cz.etnetera.reesmo.repository.mongodb.project;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import cz.etnetera.reesmo.model.mongodb.project.Project;

/**
 * Project repository
 */
@Repository
public interface ProjectRepository extends MongoRepository<Project, String>, ProjectRepositoryCustom {
	
}
