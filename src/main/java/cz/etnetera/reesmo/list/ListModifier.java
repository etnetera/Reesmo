package cz.etnetera.reesmo.list;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.AndFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.search.sort.SortBuilder;

import cz.etnetera.reesmo.list.filter.ListFilter;

/**
 * Holds data for list modification.
 * List can be filtered and sorted.
 * Or whole query can be applied to list when
 * query field is defined.
 */
public class ListModifier {
	
	private List<ListFilter> filters;
	
	private List<ListSorter> sorters;

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
	
	public ListModifier join(ListModifier modifier) {
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
	
}
