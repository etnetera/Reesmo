package com.etnetera.tremapp.repository.elasticsearch.suite;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

import com.etnetera.tremapp.model.elasticsearch.suite.Suite;

/**
 * Suite repository
 */
@Repository
public interface SuiteRepository extends ElasticsearchCrudRepository<Suite, String>, SuiteRepositoryCustom {

}
