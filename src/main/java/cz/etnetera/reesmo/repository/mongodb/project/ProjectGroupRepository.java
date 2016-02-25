package cz.etnetera.reesmo.repository.mongodb.project;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import cz.etnetera.reesmo.model.mongodb.project.ProjectGroup;

/**
 * Project group repository
 */
@Repository
public interface ProjectGroupRepository extends MongoRepository<ProjectGroup, String>, ProjectGroupRepositoryCustom {
	
	@Query(value = "{ projects : { $eq: ?0 } }")
    public List<ProjectGroup> findByProjectId(String projectId);
	
}
