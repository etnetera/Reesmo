package cz.etnetera.reesmo.repository.mongodb.project;

import cz.etnetera.reesmo.model.mongodb.project.ProjectGroup;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Project group repository
 */
@Repository
public interface ProjectGroupRepository extends MongoRepository<ProjectGroup, String>, ProjectGroupRepositoryCustom {
	
	@Query(value = "{ projects : { $eq: ?0 } }")
    public List<ProjectGroup> findByProjectId(String projectId);
	
}
