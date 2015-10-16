package com.etnetera.tremapp.web.repository.elasticsearch.suite;

import java.util.List;

import org.springframework.data.domain.Page;

import com.etnetera.tremapp.web.model.elasticsearch.suite.Suite;
import com.etnetera.tremapp.web.utils.list.ListModifier;

/**
 * Suite repository custom methods
 */
public interface SuiteRepositoryCustom {
	
	public Page<Suite> findByModifier(ListModifier modifier, List<String> allowedProjectIds);
	
}
