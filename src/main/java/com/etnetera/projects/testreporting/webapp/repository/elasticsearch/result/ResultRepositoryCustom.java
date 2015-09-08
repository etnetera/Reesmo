package com.etnetera.projects.testreporting.webapp.repository.elasticsearch.result;

import java.util.List;

import org.springframework.data.domain.Page;

import com.etnetera.projects.testreporting.webapp.model.elasticsearch.result.Result;
import com.etnetera.projects.testreporting.webapp.utils.list.ListModifier;

/**
 * Result repository custom methods
 */
public interface ResultRepositoryCustom {
	
	public Page<Result> findByModifier(ListModifier modifier, List<String> allowedProjectIds);
	
	public Page<Result> findBySuiteAndModifier(String suiteId, ListModifier modifier, List<String> allowedProjectIds);
	
	public Page<Result> findByViewAndModifier(String viewId, ListModifier modifier, List<String> allowedProjectIds);
	
}
