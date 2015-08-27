package com.etnetera.projects.testreporting.webapp.repository.elasticsearch.result;

import org.springframework.data.domain.Page;

import com.etnetera.projects.testreporting.webapp.model.elasticsearch.result.Result;
import com.etnetera.projects.testreporting.webapp.repository.elasticsearch.QueryWrapper;

/**
 * Interface holding custom ES access methods that have to be implemented by its impl.
 */
public interface ResultRepositoryCustom {
	
	public Page<Result> findByQueryWrapper(QueryWrapper queryWrapper);
	
	public Page<Result> findBySuiteAndQueryWrapper(String suiteId, QueryWrapper queryWrapper);
	
	public Page<Result> findByViewAndQueryWrapper(String viewId, QueryWrapper queryWrapper);
	
}
