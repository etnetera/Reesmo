package com.etnetera.projects.testreporting.webapp.repository.elasticsearch.suite;

import java.util.List;

import org.springframework.data.domain.Page;

import com.etnetera.projects.testreporting.webapp.model.elasticsearch.suite.Suite;
import com.etnetera.projects.testreporting.webapp.utils.list.ListModifier;

/**
 * Suite repository custom methods
 */
public interface SuiteRepositoryCustom {
	
	public Page<Suite> findByModifier(ListModifier modifier, List<String> allowedProjectIds);
	
}
