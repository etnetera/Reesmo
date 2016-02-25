package cz.etnetera.reesmo.repository.elasticsearch.result;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

import cz.etnetera.reesmo.model.elasticsearch.result.Result;

/**
 * Result repository
 */
@Repository
public interface ResultRepository extends ElasticsearchCrudRepository<Result, String>, ResultRepositoryCustom {

}
