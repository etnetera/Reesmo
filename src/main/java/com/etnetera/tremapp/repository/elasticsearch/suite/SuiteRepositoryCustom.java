package com.etnetera.tremapp.repository.elasticsearch.suite;

import java.util.List;

import org.springframework.data.domain.Page;

import com.etnetera.tremapp.list.ListModifier;
import com.etnetera.tremapp.model.elasticsearch.suite.Suite;

/**
 * Suite repository custom methods
 */
public interface SuiteRepositoryCustom {
	
	public Page<Suite> findByModifier(ListModifier modifier, List<String> allowedProjectIds);
	
}
