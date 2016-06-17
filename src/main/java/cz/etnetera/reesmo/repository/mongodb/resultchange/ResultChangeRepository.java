package cz.etnetera.reesmo.repository.mongodb.resultchange;

import cz.etnetera.reesmo.model.mongodb.resultchange.ResultChange;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Result change repository
 */
@Repository
public interface ResultChangeRepository extends MongoRepository<ResultChange, String>, ResultChangeRepositoryCustom {
	
}
