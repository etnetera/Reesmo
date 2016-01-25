package com.etnetera.tremapp.datatables.filter;

import java.util.ArrayList;
import java.util.List;

public class DatatablesFiltersDefinition {

	protected List<DatatablesFilter> filters;
	
	protected List<String> visibleFilters;

	public DatatablesFiltersDefinition() {
		this(new ArrayList<>(), new ArrayList<>());
	}
	
	public DatatablesFiltersDefinition(List<DatatablesFilter> filters, List<String> visibleFilters) {
		this.filters = filters;
		this.visibleFilters = visibleFilters;
	}

	public List<DatatablesFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<DatatablesFilter> filters) {
		this.filters = filters;
	}

	public List<String> getVisibleFilters() {
		return visibleFilters;
	}

	public void setVisibleFilters(List<String> visibleFilters) {
		this.visibleFilters = visibleFilters;
	}
	
	public DatatablesFiltersDefinition addFilter(DatatablesFilter filter) {
		return addFilter(filter, false);
	}
	
	public DatatablesFiltersDefinition addFilter(DatatablesFilter filter, boolean visible) {
		filters.add(filter);
		if (visible) visibleFilters.add(filter.getField());
		return this;
	}
	
}
