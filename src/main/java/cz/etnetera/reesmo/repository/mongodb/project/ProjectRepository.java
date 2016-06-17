package cz.etnetera.reesmo.repository.mongodb.project;

import cz.etnetera.reesmo.model.mongodb.project.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Project repository
 */
@Repository
public interface ProjectRepository extends MongoRepository<Project, String>, ProjectRepositoryCustom {
	
}
