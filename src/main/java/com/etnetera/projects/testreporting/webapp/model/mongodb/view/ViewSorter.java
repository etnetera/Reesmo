package com.etnetera.projects.testreporting.webapp.model.mongodb.view;

import org.elasticsearch.search.sort.SortBuilder;

/**
 * Sorter representation.
 * 
 * @author zdenek
 *
 */
public class ViewSorter {

	private String field;
	
	private String way;
	
	public SortBuilder getSortBuilder() {
		// TODO implement
		return null;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}
	
}
