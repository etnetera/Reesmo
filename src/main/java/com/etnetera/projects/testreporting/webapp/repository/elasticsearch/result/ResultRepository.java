package com.etnetera.projects.testreporting.webapp.repository.elasticsearch.result;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

import com.etnetera.projects.testreporting.webapp.model.elasticsearch.result.Result;

@Repository
public interface ResultRepository extends ElasticsearchCrudRepository<Result, String>, ResultRepositoryCustom {

}
