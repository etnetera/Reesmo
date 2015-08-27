package com.etnetera.projects.testreporting.webapp.repository.elasticsearch;

import java.util.List;

import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.data.domain.Pageable;

public interface QueryWrapper {

	public QueryBuilder getQueryBuilder();
	
	public FilterBuilder getFilterBuilder();
	
	public List<SortBuilder> getSortBuilders();
	
	public Pageable getPageable();
	
}
