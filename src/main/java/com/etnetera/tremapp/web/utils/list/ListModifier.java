package com.etnetera.tremapp.web.utils.list;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.AndFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Holds data for list modification.
 * List can be filtered, sorted and paged.
 * Or whole query can be applied to list when
 * query field is defined.
 * 
 * @author zdenek
 * 
 */
public class ListModifier {
	
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_SIZE = 20;
	
	private int page = -1;
	
	private int size = -1;
	
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
	
	public ListModifier join(ListModifier modifier) {
		if (modifier.page != -1) page = modifier.page;
		if (modifier.size != -1) size = modifier.size;
		
		if (filters == null) filters = modifier.filters;
		else if (modifier.filters != null) modifier.filters.forEach(f -> filters.add(f));
		
		if (sorters == null) sorters = modifier.sorters;
		else if (modifier.sorters != null) modifier.sorters.forEach(f -> sorters.add(f));
		
		return this;
	}

	public AndFilterBuilder getFilterBuilder(FilterBuilder... withFilterBuilders) {
		if ((filters == null || filters.isEmpty()) && withFilterBuilders.length == 0) {
			return null;
		}
		AndFilterBuilder filterBuilder = new AndFilterBuilder();
		if (filters != null) {
			filters.forEach(f -> filterBuilder.add(f.getFilterBuilder()));
		}
		for (FilterBuilder fb : withFilterBuilders) {
			filterBuilder.add(fb);
		}
		return filterBuilder;
	}

	public List<SortBuilder> getSortBuilders() {
		List<SortBuilder> sortBuilders = new ArrayList<>();
		if (sorters != null) {
			sorters.forEach(s -> sortBuilders.add(s.getSortBuilder()));
		}
		return sortBuilders;
	}

	public Pageable getPageable() {
		return new PageRequest(page == -1 ? DEFAULT_PAGE : page, size == -1 ? DEFAULT_SIZE : size);
	}
	
}
