package com.etnetera.tremapp.web.repository.elasticsearch.result;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

import com.etnetera.tremapp.web.model.elasticsearch.result.Result;

/**
 * Result repository
 */
@Repository
public interface ResultRepository extends ElasticsearchCrudRepository<Result, String>, ResultRepositoryCustom {

}
