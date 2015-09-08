package com.etnetera.projects.testreporting.webapp.utils.list;

import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;

/**
 * Sorter representation.
 * 
 * @author zdenek
 *
 */
public class ListSorter {

	private String field;
	
	private String way;
	
	public SortBuilder getSortBuilder() {
		return new FieldSortBuilder(field).order(SortOrder.ASC.name().toLowerCase().equals(way) ? SortOrder.ASC : SortOrder.DESC);
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
