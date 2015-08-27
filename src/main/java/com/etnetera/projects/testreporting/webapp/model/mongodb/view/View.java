package com.etnetera.projects.testreporting.webapp.model.mongodb.view;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.AndFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.joda.time.Interval;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.etnetera.projects.testreporting.webapp.model.elasticsearch.result.Result;
import com.etnetera.projects.testreporting.webapp.model.mongodb.MongoAuditedModel;
import com.etnetera.projects.testreporting.webapp.model.mongodb.user.User;
import com.etnetera.projects.testreporting.webapp.repository.elasticsearch.QueryWrapper;

/**
 * Describes stored view representing test list.
 * List can be filtered and sorted using modifiers 
 * and can has offset and be limited.
 * 
 * @author zdenek
 * 
 */
@Document
public class View extends MongoAuditedModel implements QueryWrapper {

	@Id
	private String id;
	
	/**
	 * Unique not required key for API access.
	 * Is not ID so it can be changed if needed.
	 */
	@Indexed(unique = true)
	private String key;
	
	private String name;
	
	private String description;
	
	private int page;
	
	private int size;
	
	private ViewQuery query;
	
	private List<ViewFilter> filters;
	
	private List<ViewSorter> sorters;
	
	/**
	 * If true then is available for everyone in list of views.
	 */
	private boolean shared;
	
	/**
	 * List of users with this view registered
	 */
	@DBRef
	private List<User> users;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public ViewQuery getQuery() {
		return query;
	}

	public void setQuery(ViewQuery query) {
		this.query = query;
	}

	public List<ViewFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<ViewFilter> filters) {
		this.filters = filters;
	}

	public List<ViewSorter> getSorters() {
		return sorters;
	}

	public void setSorters(List<ViewSorter> sorters) {
		this.sorters = sorters;
	}

	public boolean isShared() {
		return shared;
	}

	public void setShared(boolean shared) {
		this.shared = shared;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Result> getResults() {
		// TODO apply all modifiers and limit
		return null;
	}

	public List<Result> getChangedResults(Interval interval) {
		// TODO get results but include results with update time in given interval only
		return null;
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
		if (sorters == null || sorters.isEmpty()) {
			return null;
		}
		List<SortBuilder> sortBuilders = new ArrayList<>();
		sorters.forEach(s -> sortBuilders.add(s.getSortBuilder()));
		return sortBuilders;
	}

	@Override
	public Pageable getPageable() {
		return new PageRequest(page, size);
	}
	
}
