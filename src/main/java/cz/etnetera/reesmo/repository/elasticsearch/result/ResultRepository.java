package cz.etnetera.reesmo.repository.elasticsearch.result;

import cz.etnetera.reesmo.model.elasticsearch.result.Result;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Result repository
 */
@Repository
public interface ResultRepository extends ElasticsearchCrudRepository<Result, String>, ResultRepositoryCustom {

}
