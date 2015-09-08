package com.etnetera.projects.testreporting.webapp.repository.elasticsearch.suite;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

import com.etnetera.projects.testreporting.webapp.model.elasticsearch.suite.Suite;

/**
 * Suite repository
 */
@Repository
public interface SuiteRepository extends ElasticsearchCrudRepository<Suite, String>, SuiteRepositoryCustom {

}
