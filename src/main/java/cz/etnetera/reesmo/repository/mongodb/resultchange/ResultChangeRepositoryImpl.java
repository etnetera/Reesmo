package cz.etnetera.reesmo.repository.mongodb.resultchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 * Result change repository custom method implementation
 */
public class ResultChangeRepositoryImpl implements ResultChangeRepositoryCustom {

    @Autowired
    private MongoOperations mongoTemplate;

}
