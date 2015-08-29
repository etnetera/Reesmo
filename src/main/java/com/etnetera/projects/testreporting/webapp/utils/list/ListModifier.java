package com.etnetera.projects.testreporting.webapp.utils.list;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.AndFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.etnetera.projects.testreporting.webapp.repository.elasticsearch.QueryWrapper;

/**
 * Holds data for list modification.
 * List can be filtered, sorted and paged.
 * Or whole query can be applied to list when
 * query field is defined.
 * 
 * @author zdenek
 * 
 */
public class ListModifier implements QueryWrapper {
	
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_SIZE = 20;
	
	private int page = DEFAULT_PAGE;
	
	private int size = DEFAULT_SIZE;
	
	private ListQuery query;
	
	private List<ListFilter> filters;
	
	private List<ListSorter> sorters;
	
	public ListModifier() {}
	
	public ListModifier(int page, int size) {
		this.page = page;
		this.size = size;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public ListQuery getQuery() {
		return query;
	}

	public void setQuery(ListQuery query) {
		this.query = query;
	}

	public List<ListFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<ListFilter> filters) {
		this.filters = filters;
	}

	public List<ListSorter> getSorters() {
		return sorters;
	}

	public void setSorters(List<ListSorter> sorters) {
		this.sorters = sorters;
	}
	
	public ListModifier applyPageable(Pageable pageable) {
		page = pageable.getPageNumber();
		size = pageable.getPageSize();
		return this;
	}

	@Override
	public QueryBuilder getQueryBuilder() {
		return query == null ? null : query.getQueryBuilder();
	}

	@Override
	public FilterBuilder getFilterBuilder() {
		if (filters == null || filters.isEmpty()) {
			return null;
		}
		AndFilterBuilder filterBuilder = new AndFilterBuilder();
		filters.forEach(f -> filterBuilder.add(f.getFilterBuilder()));
		return filterBuilder;
	}

	@Override
	public List<SortBuilder> getSortBuilders() {
		List<SortBuilder> sortBuilders = new ArrayList<>();
		if (sorters != null) {
			sorters.forEach(s -> sortBuilders.add(s.getSortBuilder()));
		}
		return sortBuilders;
	}

	@Override
	public Pageable getPageable() {
		return new PageRequest(page, size);
	}
	
}
