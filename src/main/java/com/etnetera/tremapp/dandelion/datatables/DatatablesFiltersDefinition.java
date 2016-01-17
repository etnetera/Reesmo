package com.etnetera.tremapp.dandelion.datatables;

import java.util.List;

public class DatatablesFiltersDefinition {

	protected List<DatatablesFilter> filters;
	
	protected List<String> visibleFilters;

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
	
}
