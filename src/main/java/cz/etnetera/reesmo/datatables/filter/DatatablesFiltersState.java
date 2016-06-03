package cz.etnetera.reesmo.datatables.filter;

import java.util.ArrayList;
import java.util.List;

import cz.etnetera.reesmo.list.filter.ListFilter;

public class DatatablesFiltersState {

	protected List<ListFilter> filters;

	public DatatablesFiltersState() {
		this(new ArrayList<>());
	}
	
	public DatatablesFiltersState(List<ListFilter> filters) {
		this.filters = filters;
	}
	
	public List<ListFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<ListFilter> filters) {
		this.filters = filters;
	}

	public DatatablesFiltersState addFilter(ListFilter filter) {
		filters.add(filter);
		return this;
	}
	
}
