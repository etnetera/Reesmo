package cz.etnetera.reesmo.repository.mongodb.view;

import cz.etnetera.reesmo.model.mongodb.view.View;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * View repository
 */
@Repository
public interface ViewRepository extends MongoRepository<View, String>, ViewRepositoryCustom {
	
}
