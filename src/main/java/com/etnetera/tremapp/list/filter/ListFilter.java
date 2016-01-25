package com.etnetera.tremapp.list.filter;

import org.elasticsearch.index.query.FilterBuilder;

import com.etnetera.tremapp.list.filter.impl.DateRangeListFilter;
import com.etnetera.tremapp.list.filter.impl.DoubleRangeListFilter;
import com.etnetera.tremapp.list.filter.impl.PrefixListFilter;
import com.etnetera.tremapp.list.filter.impl.TermListFilter;
import com.etnetera.tremapp.list.filter.impl.TermsListFilter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Filter representation.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
	@JsonSubTypes.Type(value = PrefixListFilter.class, name = PrefixListFilter.TYPE),
	@JsonSubTypes.Type(value = TermListFilter.class, name = TermListFilter.TYPE),
	@JsonSubTypes.Type(value = TermsListFilter.class, name = TermsListFilter.TYPE),
	@JsonSubTypes.Type(value = DoubleRangeListFilter.class, name = DoubleRangeListFilter.TYPE),
	@JsonSubTypes.Type(value = DateRangeListFilter.class, name = DateRangeListFilter.TYPE)
}) 
abstract public class ListFilter {

	protected String field;
	
	abstract public FilterBuilder getFilterBuilder();

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	
}
