package com.etnetera.projects.testreporting.webapp.model.mongodb.view;

import org.elasticsearch.index.query.FilterBuilder;

/**
 * Filter representation.
 * 
 * @author zdenek
 *
 */
public class ViewFilter {

	private String field;
	
	private String comparator;
	
	private String value;
	
	public FilterBuilder getFilterBuilder() {
		// TODO implement
		return null;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getComparator() {
		return comparator;
	}

	public void setComparator(String comparator) {
		this.comparator = comparator;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
