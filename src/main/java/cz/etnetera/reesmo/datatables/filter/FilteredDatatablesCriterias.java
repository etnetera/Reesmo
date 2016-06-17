package cz.etnetera.reesmo.datatables.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.core.util.Validate;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import cz.etnetera.reesmo.list.ListModifier;
import cz.etnetera.reesmo.list.filter.ListFilter;
import org.elasticsearch.index.query.AndFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class FilteredDatatablesCriterias {

	protected DatatablesCriterias criterias;

	protected List<ListFilter> filters;
	
	protected ListModifier modifier;

	public FilteredDatatablesCriterias(DatatablesCriterias criterias, List<ListFilter> filters) {
		this.criterias = criterias;
		this.filters = filters;
		modifier = new ListModifier();
		modifier.setFilters(filters);
	}

	public DatatablesCriterias getCriterias() {
		return criterias;
	}

	public void setCriterias(DatatablesCriterias criterias) {
		this.criterias = criterias;
	}

	public List<ListFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<ListFilter> filters) {
		this.filters = filters;
	}
	
	public AndFilterBuilder getFilterBuilder(FilterBuilder... withFilterBuilders) {
		return modifier.getFilterBuilder(withFilterBuilders);
	}
	
	/**
	 * Load list filters from request.
	 * 
	 * @param request
	 *            The request sent containing list filters.
	 * @return list of filters.
	 */
	public static List<ListFilter> getFiltersFromRequest(HttpServletRequest request) throws Exception {
		Validate.notNull(request, "The HTTP request cannot be null");

		String paramFiltersCnt = request.getParameter("filtersCnt");
		Integer filtersCnt = StringUtils.isNotBlank(paramFiltersCnt) ? Integer.parseInt(paramFiltersCnt) : 0;
		
		if (filtersCnt < 1) return null;
		boolean error = false;
		List<ListFilter> filters = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		
		for (int i = 0; i < filtersCnt; i++) {
			String filterStr = request.getParameter("filters[" + i + "]");
			if (StringUtils.isBlank(filterStr)) {
				error = true;
				break;
			}
			filters.add(mapper.readValue(filterStr, ListFilter.class));
		}
		
		return error ? null : filters;
	}

	/**
	 * <p>
	 * Map all request parameters into a wrapper POJO that eases SQL querying.
	 * </p>
	 * 
	 * @param request
	 *            The request sent by Datatables containing all parameters.
	 * @return a wrapper POJO.
	 */
	public static FilteredDatatablesCriterias getFromRequest(HttpServletRequest request) throws Exception {
		return new FilteredDatatablesCriterias(DatatablesCriterias.getFromRequest(request), getFiltersFromRequest(request));
	}

}
